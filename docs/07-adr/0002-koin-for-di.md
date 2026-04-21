# ADR 0002: Koin for dependency injection

- **Status:** Accepted
- **Date:** 2026-04-21

## Context

We need a DI container that works in `commonMain` (Kotlin Multiplatform), on Android (with ViewModel scoping and Compose ergonomics) and on iOS (callable from Swift). The realistic options were Koin, Kodein, and Hilt.

Hilt is Android-only — unusable in shared code, so it would force us to reintroduce a second container on iOS and duplicate bindings. Kodein is KMP-capable but has lower adoption, thinner Compose/ViewModel integrations, and no equivalent of `koin-androidx-compose`'s `koinViewModel()`. Koin has first-class KMP support (`koin-core`), a Compose integration that handles `ViewModelStoreOwner` for us, and a stable Swift-side call path via `KoinPlatform.getKoin().get()`.

## Decision

Use Koin 4 everywhere. Declare modules per layer / feature (`coreModule`, `platformModule`, `todoModule`, `androidModule`). `TemplateApplication.startKoin { ... }` boots Android; `KoinInitializer().doInit()` boots iOS. A tiny `KoinBridge` object in `iosMain` exposes the ViewModels Swift needs, so SwiftUI never imports Koin types directly.

## Consequences

**Positive.** One DSL, one mental model across platforms. `koinViewModel()` on Android gives us lifecycle-correct VMs without handwritten factories. Parameterised VMs work via `parametersOf(...)` on both sides. Tests replace modules easily.

**Negative.** Koin resolves at runtime, so a missing binding is a crash, not a compile error — we pay the usual service-locator tax. iOS callers must go through `KoinBridge` rather than resolving arbitrary Kotlin types, because SKIE doesn't expose Koin's generic `get<T>()` nicely. Minor; worth it for the uniform cross-platform story.
