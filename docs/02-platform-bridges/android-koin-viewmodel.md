# Android Koin ViewModel integration

After reading this you'll know how a Compose screen pulls a Kotlin ViewModel out of Koin, collects state safely, and handles side-effects.

## How the VM is resolved

The `koin-androidx-compose` artefact (pinned in `gradle/libs.versions.toml` as `koinCompose = "4.0.0"`) adds `koinViewModel()` — a Compose binding that defers to `ViewModelStoreOwner` so the instance survives config changes.

```kotlin
import org.koin.androidx.compose.koinViewModel

@Composable
fun TodoListScreen(
    onNavigate: (Destination) -> Unit,
    vm: TodoListViewModel = koinViewModel(),
) { ... }
```

For parameterised VMs (e.g. the detail screen takes an optional `id`), pass `parameters = { parametersOf(id) }` — see `TodoDetailScreen.kt`.

## Collecting state and side-effects

From `androidApp/src/main/kotlin/com/electra/template/android/features/todo/list/TodoListScreen.kt`:

```kotlin
val state by vm.state.collectAsStateWithLifecycle()
var showQuickAdd by rememberSaveable { mutableStateOf(false) }

LaunchedEffect(vm) {
    vm.effects.collect { e ->
        when (e) {
            is TodoListSideEffect.NavigateToDetail -> onNavigate(Destination.TodoDetail(e.id))
            TodoListSideEffect.OpenQuickAdd        -> showQuickAdd = true
            is TodoListSideEffect.ShowError        -> { /* snackbar hook */ }
        }
    }
}

TodoListView(
    state = state,
    actions = TodoListActions(
        onRefresh           = vm::onRefresh,
        onCreate            = vm::onRequestQuickAdd,
        onSelect            = vm::onSelect,
        onToggle            = vm::onToggle,
        onDelete            = vm::onDelete,
        onToggleDoneSection = vm::onToggleDoneSection,
    ),
)
```

The "new todo" FAB binds to `onRequestQuickAdd`, which emits the `OpenQuickAdd` side-effect; the screen reacts by showing a bottom-sheet `QuickAddSheet` rather than navigating to a detail screen with a null id.

`collectAsStateWithLifecycle` (from `androidx.lifecycle.runtime.compose`) pauses collection when the screen is off-screen, avoiding wasted recompositions. `LaunchedEffect(vm)` launches exactly once per VM instance and is cancelled when the composition leaves, matching the effect lifetime to the screen.

## Wiring

Factories are declared in `data/todo/TodoModule.kt` with `viewModel { TodoListViewModel(get(), get(), get(), get()) }`. `TemplateApplication` calls `startKoin { modules(coreModule, platformModule, todoModule, androidModule) }`.
