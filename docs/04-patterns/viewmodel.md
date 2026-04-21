# ViewModel pattern

After reading this you'll know when to update state vs. emit a side-effect, and the exact base-class contract.

## The base class

`shared/src/commonMain/kotlin/com/electra/template/presentation/base/BaseViewModel.kt`:

```kotlin
abstract class BaseViewModel<State : Any, SideEffect : Any> : ViewModel() {
    protected abstract val initialState: State

    private val _state by lazy { MutableStateFlow(initialState) }
    val state: StateFlow<State> by lazy { _state.asStateFlow() }

    private val _effects = MutableSharedFlow<SideEffect>(extraBufferCapacity = 8)
    val effects: SharedFlow<SideEffect> = _effects.asSharedFlow()

    protected val scope: CoroutineScope get() = viewModelScope

    protected fun update(reducer: (State) -> State) { _state.update(reducer) }
    protected suspend fun emit(effect: SideEffect) { _effects.emit(effect) }
}
```

It inherits from the JetBrains KMP fork of `androidx.lifecycle.ViewModel` (`libs.jb.lifecycle.viewmodel`), so `viewModelScope` is available in `commonMain`. The `_state` flow is lazy so subclasses can set `initialState` after `super()` has run.

## Update state, or emit a side-effect?

- **Update state** when the change should be visible on the next recomposition and survive backgrounding / rotations: loading flags, fetched data, validation errors next to a field.
- **Emit a side-effect** for one-shots the UI layer must react to at most once: navigation, toasts/snackbars, dismissal, haptic. Late subscribers do not replay the event (that's what `SharedFlow` with `extraBufferCapacity = 8` and no replay gives you — see `BaseViewModel.kt`).

## Example

From `TodoListViewModel.kt`:

```kotlin
fun onRefresh() {
    update { it.copy(status = UiStatus.Loading) }  // state
    scope.launch {
        try {
            getTodos.refresh()
            update { it.copy(status = UiStatus.Idle) }
        } catch (e: AppException) {
            update { it.copy(status = UiStatus.Error(e.message ?: "error", retryable = true)) }
            emit(TodoListSideEffect.ShowError(e.message ?: "error"))  // side-effect
        }
    }
}
```

The error is surfaced **both** as state (so the list can render a banner) and as a side-effect (so a transient snackbar can appear). That duplication is intentional — side-effects should not be the only carrier of user-facing state.
