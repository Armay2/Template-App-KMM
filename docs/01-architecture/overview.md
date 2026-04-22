# Architecture overview

After reading this you'll know the Gradle module layout, the four layers inside each feature, and how a user intent travels through them.

## Modules

The repo is a multi-module Gradle build (`settings.gradle.kts`):

- `:core` — Kotlin Multiplatform library with shared infrastructure (`BaseViewModel`, `AppException`, `HttpClientFactory`, `Logger`, `Analytics`, `CrashReporter`, `KeyValueStore`, `SettingsFactory`, `DispatcherProvider`, `UiStatus`, `DesignTokens`). No feature code.
- `:feature:<name>` — one module per feature, each holding its own `domain`, `data`, and `presentation` (VM). `:feature:todo` is the reference.
- `:shared` — iOS framework umbrella. Holds only app-wide navigation (`Destination`, `DeepLinkParser`) and the iOS DI bridge (`KoinBridge`, `KoinInitializer`), and re-exports `:core` + every `:feature:*` so Xcode sees one `Shared.framework`.
- `:androidApp` — Compose / Material 3 app depending on `:shared` + each `:feature:*`.
- `:iosApp` — SwiftUI app linking `Shared.framework`, produced by Xcode with SKIE-post-processed bindings.

Two Gradle convention plugins in `build-logic/convention/` keep per-module build files tiny: `template.kmp.library` (generic KMP lib) and `template.kmp.feature` (adds `api(:core)` + standard test deps). See [`modularization.md`](modularization.md) and [ADR-0007](../07-adr/0007-feature-modularization.md).

## Feature layers

Inside `feature/<name>/src/commonMain/kotlin/com/electra/template/`:

- `domain/<name>/` — plain entities, repository interfaces, use cases.
- `data/<name>/` — Ktor API, DTOs, mappers, repository impl, Koin module.
- `presentation/<name>/` — `State`, `SideEffect`, `ViewModel` (extends `BaseViewModel` from `:core`), `Fakes` for previews/tests.

## Diagram

```
+-------------------------------------------------------------+
|                      androidApp (Compose)                   |
|   MainActivity -> AppNavHost -> TodoListScreen (container)  |
|                               -> TodoListView    (pure)     |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                   :shared (iOS framework umbrella)          |
|   core/navigation  -> Destination, DeepLinkParser           |
|   iosMain          -> KoinBridge, KoinInitializer           |
|                       api(:core) + api(:feature:*)          |
|                       export(:core) + export(:feature:*)    |
+-------------------------------------------------------------+
|                    :feature:todo                            |
|   presentation  -> TodoListViewModel, TodoDetailViewModel   |
|   domain        -> Todo, TodoRepository, *UseCase           |
|   data          -> TodoApi (Ktor) -> TodoRepositoryImpl     |
+-------------------------------------------------------------+
|                    :core                                    |
|   BaseViewModel, AppException, HttpClientFactory,           |
|   Logger, Analytics, CrashReporter, KeyValueStore,          |
|   SettingsFactory, DispatcherProvider, DesignTokens         |
+-------------------------------------------------------------+
                              ^
                              |
+-------------------------------------------------------------+
|                       iosApp (SwiftUI)                      |
|   iosAppApp -> RootView -> TodoListScreen (container)       |
|                         -> TodoListView   (pure)            |
|   Observed<State, Effect, VM> wraps the Kotlin VM.          |
+-------------------------------------------------------------+
```

## Split rationale

Everything up to and including the ViewModel is shared — see `shared-up-to-viewmodel.md`. Screens below are native because platform-idiomatic UI (Liquid Glass, Material 3 Expressive, predictive back, `.task { await ... }`) is easier to get right natively than through a single cross-platform UI toolkit.

See `data-flow.md` for the request/response cycle and `navigation.md` for how `Destination` crosses the bridge.
