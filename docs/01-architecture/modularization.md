# Modularization

After reading this you'll know the three-way Gradle split (`:core`, `:feature:*`, `:shared`), what lives where, and how the convention plugins in `build-logic/` keep module build files tiny.

See [ADR-0007](../07-adr/0007-feature-modularization.md) for the "why". See [`03-adding-a-feature/`](../03-adding-a-feature/) for the step-by-step "how to add one".

## Module hierarchy

```
build-logic/convention/    — Gradle convention plugins
  ├── KmpLibraryConventionPlugin     → id: template.kmp.library
  └── KmpFeatureConventionPlugin     → id: template.kmp.feature

:core         — shared infrastructure (no feature code, no UI logic)
  ↑                     ↑
:feature:todo          :feature:<next>   (each depends on :core via template.kmp.feature)
  ↑
:shared       — iOS framework umbrella + navigation + iOS DI bridge
                api(:core) + api(:feature:todo) + export() both in the iOS framework
  ↑
:androidApp    (Jetpack Compose UI — depends on :shared + :feature:todo)
:iosApp        (SwiftUI UI — imports only Shared.framework)
```

## What goes where

### `:core` — cross-cutting infrastructure

`core/src/commonMain/kotlin/com/electra/template/core/`:

- `BaseViewModel`, `UiStatus` — presentation primitives.
- `AppException`, `ErrorMapper` — typed error model.
- `HttpClientFactory` — Ktor client construction.
- `Logger`, `Analytics`, `CrashReporter` — observability abstractions (no-op by default).
- `KeyValueStore`, `SettingsFactory` — persistence abstractions over `multiplatform-settings`.
- `DispatcherProvider` — coroutine dispatcher injection point.
- `DesignTokens` — shared typography/spacing/color tokens.
- `CoreModule` — Koin module exposing the above.

Platform implementations (`androidMain/`, `iosMain/`) provide the concrete `SettingsFactory`, `Logger`, etc.

**`:core` holds no feature code and no UI logic.** If it looks feature-specific, it belongs in a feature module.

### `:feature:todo` — a reference feature

`feature/todo/src/commonMain/kotlin/com/electra/template/`:

- `domain/todo/` — `Todo`, `TodoRepository`, use cases.
- `data/todo/` — `TodoDto`, `TodoApi`, `TodoMapper`, `TodoRepositoryImpl`, `TodoModule` (Koin).
- `presentation/todo/` — `TodoListState`, `TodoListSideEffect`, `TodoListViewModel`, `TodoListFakes` (and same for `detail/`).

Tests live in `feature/todo/src/commonTest/`.

### `:shared` — iOS framework umbrella

`shared/src/commonMain/kotlin/com/electra/template/`:

- `core/navigation/Destination.kt` — app-wide sealed interface of routes.
- `core/navigation/DeepLinkParser.kt` — URI → `Destination` translation.

`shared/src/iosMain/kotlin/com/electra/template/`:

- `KoinBridge.kt` — iOS-facing DI façade (exposes VM factories to Swift).
- `KoinInitializer.kt` — boots Koin on iOS with `coreModule + todoModule + ...`.

`:shared` is deliberately thin. It depends on `:core` and every `:feature:*` via `api(...)`, and re-exports them in the iOS framework block so Swift sees `Todo`, `TodoListViewModel`, `AppException`, etc. through one `import Shared`.

## Convention plugins

Both plugins live in `build-logic/convention/` and are applied via plugin id in each module's `build.gradle.kts`. They're wired through an included build in the root `settings.gradle.kts`.

### `template.kmp.library`

Applied by `:core` and `:shared`. Configures:

- `org.jetbrains.kotlin.multiplatform` with Android + iOS (`iosX64`, `iosArm64`, `iosSimulatorArm64`) targets.
- Android library plugin with consistent `compileSdk` / `minSdk` / JVM target.
- Standard `commonMain` / `commonTest` source-set shape.

Module build files become ~5 lines (plugin + `android { namespace = ... }`).

### `template.kmp.feature`

Applied by every `:feature:*`. Does everything `template.kmp.library` does, plus:

- Adds `api(project(":core"))` automatically — every feature gets `BaseViewModel`, `AppException`, `HttpClientFactory`, etc. without declaring it.
- Wires standard `commonTest` deps: `kotlin-test`, `kotlinx-coroutines-test`, `turbine`.

A feature's `build.gradle.kts` is:

```kotlin
plugins { id("template.kmp.feature") }

android { namespace = "com.electra.template.feature.<name>" }
```

## iOS framework strategy

Xcode links exactly one `Shared.framework`. The iOS `framework { }` block in `shared/build.gradle.kts` does:

```kotlin
export(project(":core"))
export(projects.feature.todo)
// export(projects.feature.<next>)  -- one line per feature
```

`export()` (not just `api()`) is what makes the exported types appear in the generated Objective-C/Swift headers. Without it, Swift code sees `KoinBridge` but not the `Todo` type it returns.

Trade-off: `:shared` knows about every feature (one `api()` + one `export()` line). The alternative — one framework per feature — multiplies Xcode config, inflates app size, and forces feature-aware imports in Swift. We accept one line per feature in `shared/build.gradle.kts` as the price of a single `import Shared`.

## Related

- [ADR-0007](../07-adr/0007-feature-modularization.md) — why this split.
- [`03-adding-a-feature/step-by-step.md`](../03-adding-a-feature/step-by-step.md) — add a feature end-to-end.
- [`overview.md`](overview.md) — request-to-render flow across the three modules.
