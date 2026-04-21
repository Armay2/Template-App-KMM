# ADR 0006: Screen / View split per feature

- **Status:** Accepted
- **Date:** 2026-04-21

## Context

A feature composable/view that owns its ViewModel, collects its state, and renders UI is easy to write but hard to preview and hard to unit-test: the DI graph, lifecycle collection, and navigation side-effects all depend on live infrastructure. Compose `@Preview` and SwiftUI `#Preview` must render synchronously against plain data; they can't stand up Koin or wait for flows.

We also want one set of state fixtures reused by previews, UI tests, and shared ViewModel tests — not three near-duplicates.

## Decision

Every feature gets two files per platform:

- **Screen** (container): resolves the ViewModel via `koinViewModel()` or `KoinBridge`, subscribes to state and effects, handles navigation. Not previewable.
- **View** (pure): takes `state: <Feature>State` and `actions: <Feature>Actions` (a struct of callbacks). Has `@Preview` / `#Preview` entries that feed `<Feature>Fakes` instances.

Fakes are single-source in `shared/src/commonMain/kotlin/com/electra/template/presentation/<feature>/<Feature>Fakes.kt` and consumed by Compose previews, SwiftUI previews, and `commonTest` / Compose UI tests alike.

## Consequences

**Positive.** Designers can tweak the pure View with live previews. Compose UI tests and XCUITest smoke tests run against fakes without standing up the app. Changes to actions are compiler-caught on both platforms via the `Actions` struct. ViewModel tests stay in `commonTest` and reuse the same fakes, so empty / loading / populated / error scenarios are defined once.

**Negative.** Two files per platform for every feature — four total. That's a maintenance tax, especially for trivial screens. Forwarding props through the container adds a small amount of boilerplate (`actions = Actions(...)`). We accept this cost: the testability and design-review velocity it unlocks dominate the marginal file count.
