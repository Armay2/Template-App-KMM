# Adding a feature end-to-end

After reading this you'll have a deterministic recipe for slotting a new feature — say, `Project` — into all three modules.

Mirror the existing `todo` tree. Each step produces an independently compilable unit; run `./gradlew :shared:build` between steps as a sanity check.

## 1. Domain entity

`shared/src/commonMain/kotlin/com/electra/template/domain/project/Project.kt`:

```kotlin
data class Project(val id: String, val name: String, val archived: Boolean)
```

## 2. Repository interface

`shared/src/commonMain/kotlin/com/electra/template/domain/project/ProjectRepository.kt`. Annotate every `suspend fun` with `@Throws(AppException::class, CancellationException::class)` — mirror `TodoRepository.kt`.

## 3. Data DTOs + API + mappers

Mirror `data/todo/`:

- `ProjectDto.kt` — `@Serializable` with kotlinx-serialization.
- `ProjectApi.kt` — thin Ktor wrapper with `wrap { ... }.toAppException()` exception translation.
- `ProjectMapper.kt` — `fun ProjectDto.toDomain()` / `fun Project.toDto()`.

## 4. Repository implementation

`data/project/ProjectRepositoryImpl.kt`. Keep an in-memory `MutableStateFlow<List<Project>>` as the public `projects` Flow; update the cache after each successful remote call. Use `TodoRepositoryImpl.kt` as the reference shape.

## 5. Use cases

`domain/project/usecase/`. One file per verb (`GetProjects`, `CreateProject`, `ArchiveProject`). Skip a use-case if it's a pure 1-line pass-through and you have no validation to add — see ADR 0006 on use-case ergonomics.

## 6. Koin module

`data/project/ProjectModule.kt` (mirror `TodoModule.kt`). Declare `single<ProjectRepository>`, `single<ProjectApi>`, `factory` per use-case, `viewModel` for each VM. Register the new module in both hosts:

- Android: `TemplateApplication.onCreate` → `modules(..., projectModule)`.
- iOS: `shared/src/iosMain/.../KoinInitializer.kt` → `modules(..., projectModule)`.

## 7. Presentation layer (shared)

Under `presentation/project/list/`:

- `ProjectListState.kt` — `data class` with the view state.
- `ProjectListSideEffect.kt` — `sealed interface` with navigation / toast events.
- `ProjectListViewModel.kt` — extends `BaseViewModel<State, SideEffect>`. Call `update { }` to reduce and `emit(...)` to fire events.
- `ProjectListFakes.kt` — curated instances for previews and tests.

## 8. Navigation

Add the new destination to `core/navigation/Destination.kt` (`data object ProjectList : Destination`, `data class ProjectDetail(val id: String?) : Destination`) and extend `DeepLinkParser`.

- Android: extend `Routes`, `Destination.toRoute()`, and add `composable(...)` in `AppNavHost.kt`.
- iOS: add cases to `Route` enum in `AppNavigator.swift` + the `init?(from:)`, then update `DestinationRouter.swift`.

## 9. Native screens

Duplicate the Todo pattern:

- Android: `ProjectListScreen` (container: `koinViewModel()` + `collectAsStateWithLifecycle` + `LaunchedEffect`) and `ProjectListView` (pure, takes `state` + `actions`, provides `@Preview`).
- iOS: `ProjectListScreen` (container: `KoinBridge.shared.projectListViewModel()` + `Observed`) and `ProjectListView` (pure, `struct ProjectListView: View`, `#Preview`).

## 10. Tests

- `commonTest/.../ProjectRepositoryImplTest.kt` with `FakeProjectApi` via Ktor `MockEngine` (see `FakeTodoApi.kt`).
- `commonTest/.../ProjectListViewModelTest.kt` using `StandardTestDispatcher`, `runTest`, and Turbine for effects.
- Android: a Compose UI test asserting the empty/items variants of `ProjectListView`.
- iOS: an XCUITest smoke-checking the navigation bar title if the feature is launchable from root.

Run `./gradlew :shared:allTests :androidApp:assembleDebug` and build iOS in Xcode. Add an entry to `CHANGELOG.md`. Done.

See `checklist.md` for the shorter PR-review version.
