# SKIE configuration

After reading this you'll know which SKIE version is used, what it gives you for free, and the two Swift-visible naming quirks to remember.

## Version

`gradle/libs.versions.toml` pins `skie = "0.10.11"`. This release targets Kotlin 2.1 and is the first SKIE line fully compatible with **Swift 6 strict concurrency** — the flows it generates are `Sendable`, and the suspend shims it emits are safe to `await` from `@MainActor` closures.

Application: the plugin is applied in `shared/build.gradle.kts` via `alias(libs.plugins.skie)`. No extra DSL block is needed; defaults are good.

## What SKIE does automatically

- Kotlin `sealed interface` / `sealed class` → Swift enum-like exhaustive switches (`switch effect { case let e as TodoListSideEffectNavigateToDetail: ... }`).
- `suspend fun` → Swift `async throws` with proper isolation propagation.
- `Flow<T>` / `StateFlow<T>` / `SharedFlow<T>` → Swift `AsyncSequence`, consumable with `for await` (used in `Observed.swift`).
- Typed `@Throws` annotations on Kotlin propagate to Swift `throws` rather than a generic `Error`.
- Default-argument overload bridging so Kotlin functions with default parameters are callable idiomatically from Swift.

## Naming quirks

Two surprises the template lives with:

- **Sealed object case access** is flattened. `UiStatus.Idle` becomes `UiStatusIdle.shared` in Swift — see `Features/Todo/List/TodoListView.swift` and `#Preview("Empty")` for a concrete use.
- **Reserved Swift keywords on data-class fields** get a trailing underscore. `TodoDetailState.description` becomes `state.description_` in Swift (see `Features/Todo/Detail/TodoDetailView.swift`).

When adding new state fields or sealed types, watch for these; they're the only renames that routinely bite during interop. No opt-out is needed — both transformations are the SKIE defaults.
