# Architecture overview

After reading this you'll know the three Gradle modules, the four layers inside `shared`, and how a user intent travels through them.

## Modules

The repo is a three-module Gradle build (`settings.gradle.kts`):

- `shared` — Kotlin Multiplatform library (Android AAR + iOS XCFramework, packaged as `Shared.framework`).
- `androidApp` — Compose / Material 3 app that depends on `:shared`.
- `iosApp` — SwiftUI app linking `Shared.framework`, produced by Xcode with SKIE-post-processed bindings.

## Shared layers

Inside `shared/src/commonMain/kotlin/com/electra/template/`:

- `core/` — cross-cutting: `DesignTokens`, `Destination`, `AppException`, `Logger`, `AnalyticsTracker`, `CrashReporter`, `DispatcherProvider`, DI modules.
- `data/` — Ktor client, DTOs, repositories, `KeyValueStore` over `multiplatform-settings`.
- `domain/` — plain entities (`Todo`), repository interfaces, use-cases.
- `presentation/` — `BaseViewModel<State, SideEffect>`, `UiStatus`, feature VMs and `*Fakes.kt` for previews/tests.

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
|                         shared (KMM)                        |
|   presentation  -> BaseViewModel<State, SideEffect>         |
|                 -> TodoListViewModel, TodoDetailViewModel   |
|   domain        -> Todo, TodoRepository, *UseCase           |
|   data          -> TodoApi (Ktor) -> TodoRepositoryImpl     |
|                 -> KeyValueStore (Settings)                 |
|   core          -> AppException, Destination, DI, tokens    |
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
