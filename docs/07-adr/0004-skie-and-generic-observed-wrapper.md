# ADR 0004: SKIE plus a generic `Observed` wrapper for SwiftUI

- **Status:** Accepted
- **Date:** 2026-04-21

## Context

Kotlin/Native exposes Kotlin types to Swift with rough ergonomics: no sealed enums, no `async`/`await` bridging, no `AsyncSequence` for flows, ObjC-name-mangled generics. SwiftUI, on the other hand, tracks state changes via `@Observable` on the main actor and expects `Sendable` types in Swift 6 strict concurrency.

We need ViewModels that (a) are written once in Kotlin, (b) feel native from Swift, and (c) survive Swift 6 strict concurrency without `@unchecked Sendable` escape hatches. Writing a per-feature bespoke ObservableObject for each Kotlin VM would be boilerplate we'd regret.

## Decision

Use [SKIE](https://skie.touchlab.co) (pinned at 0.10.11) to translate Kotlin sealed types, suspend functions, and flows. On top of SKIE, ship a single generic `Observed<State, Effect, VM: BaseViewModel<State, Effect>>` class in `iosApp/.../Presentation/Base/Observed.swift`. It is `@MainActor @Observable`, subscribes to `vm.state` and `vm.effects` via SKIE-generated `AsyncSequence`s, mirrors state into a tracked property, and cancels its tasks in `isolated deinit`.

## Consequences

**Positive.** One wrapper for every feature; adding a new VM in Swift is three lines. Swift 6 strict concurrency compiles clean. SKIE handles the painful Kotlin-to-Swift bits (sealed subclass casts, typed `throws`, `suspend` bridging). The wrapper is generic over `<State, Effect, VM>`, so there's no duplication per feature.

**Negative.** We're coupled to SKIE's compatibility with Kotlin releases; upgrading Kotlin generally waits for SKIE. SKIE renames some symbols (`UiStatus.Idle` → `UiStatusIdle.shared`, `description` → `description_`), which surprises engineers new to the template. Both are documented in `docs/02-platform-bridges/skie-configuration.md`. On balance, the ergonomic win pays for the coupling.
