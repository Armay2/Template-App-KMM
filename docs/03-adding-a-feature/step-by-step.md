# Adding a feature end-to-end

After reading this you'll have a deterministic recipe for slotting a new feature — say, `Profile` — into the 3-module architecture (`:core`, `:feature:*`, `:shared`, plus the two apps).

Mirror the existing `feature/todo` tree. Each step produces an independently compilable unit; run `./gradlew :feature:profile:build` between steps as a sanity check.

See [`docs/01-architecture/modularization.md`](../01-architecture/modularization.md) for the architectural background.

## 1. Create the module directory

```bash
mkdir -p feature/profile/src/commonMain/kotlin/com/electra/template/{domain,data,presentation}/profile
mkdir -p feature/profile/src/commonTest/kotlin/com/electra/template/{domain,data,presentation}/profile
```

## 2. Create `feature/profile/build.gradle.kts`

```kotlin
plugins { id("template.kmp.feature") }

android { namespace = "com.electra.template.feature.profile" }
```

The `template.kmp.feature` convention plugin applies Kotlin Multiplatform, the Android library, a standard source-set layout, `api(project(":core"))`, and the `commonTest` deps. Add Ktor or other libraries only if the feature needs something beyond what `:core` exposes transitively.

## 3. Register the module

In `settings.gradle.kts`, add:

```kotlin
include(":feature:profile")
```

## 4. Wire the feature into `:shared`

In `shared/build.gradle.kts`:

- Add `api(projects.feature.profile)` in the `commonMain` dependencies block.
- Add `export(projects.feature.profile)` inside the iOS framework block so Swift sees the feature's types.

## 5. Expose to `:androidApp`

In `androidApp/build.gradle.kts`, add:

```kotlin
implementation(project(":feature:profile"))
```

Explicit even though transitive via `:shared` — it makes the Android-side dependency graph legible.

## 6. Domain

`feature/profile/src/commonMain/kotlin/com/electra/template/domain/profile/`:

- `Profile.kt` — `data class Profile(val id: String, val displayName: String, val avatarUrl: String?)`.
- `ProfileRepository.kt` — interface. Annotate every `suspend fun` with `@Throws(AppException::class, CancellationException::class)` (mirror `TodoRepository.kt`).
- `usecase/` — one verb per file (`GetProfile`, `UpdateDisplayName`). Skip a use case if it's a 1-line pass-through with no validation.

## 7. Data

`feature/profile/src/commonMain/kotlin/com/electra/template/data/profile/`:

- `ProfileDto.kt` — `@Serializable`.
- `ProfileApi.kt` — thin Ktor wrapper using `HttpClientFactory` from `:core`; translate with `wrap { ... }.toAppException()`.
- `ProfileMapper.kt` — `fun ProfileDto.toDomain()` / `fun Profile.toDto()`.
- `ProfileRepositoryImpl.kt` — in-memory cache as `MutableStateFlow<Profile?>`, updated after each successful remote call.
- `ProfileModule.kt` — Koin module: `single<ProfileRepository>`, `single<ProfileApi>`, `factory` per use case, `viewModel` per VM.

## 8. Presentation

`feature/profile/src/commonMain/kotlin/com/electra/template/presentation/profile/`:

- `ProfileState.kt` — `data class` holding the view state.
- `ProfileSideEffect.kt` — `sealed interface` for navigation / toast events.
- `ProfileViewModel.kt` — extends `BaseViewModel<State, SideEffect>` from `:core`. Call `update { }` to reduce, `emit(...)` to fire effects.
- `ProfileFakes.kt` — curated instances for previews and tests (empty / loading / populated / error).

## 9. Tests

`feature/profile/src/commonTest/.../`:

- `ProfileRepositoryImplTest.kt` — `FakeProfileApi` via Ktor `MockEngine` (see `FakeTodoApi.kt`).
- `ProfileViewModelTest.kt` — `StandardTestDispatcher`, `runTest`, Turbine for effects.

## 10. Navigation

Because navigation types are app-wide, extend them in `:shared`:

- `shared/src/commonMain/.../core/navigation/Destination.kt` — add `data object ProfileHome : Destination` (and any deep-linkable variants).
- `shared/src/commonMain/.../core/navigation/DeepLinkParser.kt` — extend if the feature is deep-linkable.

Then platform navigation:

- Android: extend `Routes`, `Destination.toRoute()`, and add a `composable(...)` entry in `AppNavHost.kt`.
- iOS: add cases to the `Route` enum in `AppNavigator.swift` and its `init?(from:)`, then extend `DestinationRouter.swift`.

## 11. Wire Koin

- Android: `TemplateApplication.onCreate` → `modules(..., profileModule)`.
- iOS: `shared/src/iosMain/.../KoinInitializer.kt` → `modules(..., profileModule)`.

## 12. Native screens

Mirror the Todo pattern (see [ADR-0006](../07-adr/0006-screen-vs-view-split.md)).

- Android: `androidApp/src/main/kotlin/com/electra/template/android/features/profile/`
  - `ProfileScreen` (container: `koinViewModel()` + `collectAsStateWithLifecycle` + `LaunchedEffect`)
  - `ProfileView` (pure, takes `state` + `actions`, provides `@Preview` entries using `ProfileFakes`).
- iOS: `iosApp/iosApp/Features/Profile/`
  - `ProfileScreen.swift` (container: `KoinBridge.shared.profileViewModel()` + `Observed`)
  - `ProfileView.swift` (pure, `struct ProfileView: View`, `#Preview` entries).

Run `./gradlew :feature:profile:allTests :androidApp:assembleDebug`, build iOS in Xcode, update `CHANGELOG.md`, done.

See [`checklist.md`](checklist.md) for the shorter PR-review version.

## Common pitfalls

- **Don't** add `implementation(project(":shared"))` to your feature — `:shared` already depends on your feature, so this creates a cycle. The convention plugin wires `api(project(":core"))` for you; that's all you need.
- **Don't** put UI code in the feature module — UI stays native (`androidApp/` + `iosApp/`). The feature module stops at the ViewModel, consistent with [ADR-0001](../07-adr/0001-kmm-shared-up-to-viewmodel.md).
- **Don't** forget `export(projects.feature.<name>)` in `shared/build.gradle.kts`'s iOS framework block. Without it, Swift sees `KoinBridge` but not the types it returns, and the generated headers miss your feature entirely.
