# ADR 0007: Feature-based modularization with a `:core` umbrella

- **Status:** Accepted
- **Date:** 2026-04-22

## Context

The template started as a mono-module `shared/` holding domain, data, and presentation for every feature. That shape is fine up to roughly 10–15 features; past that, compile-incremental benefits erode (every touch rebuilds the whole library), symbol visibility noise grows (everything's `internal` or leaked), and feature boundaries become a matter of folder discipline rather than a compiler-enforced contract.

At the other extreme, NowInAndroid-style layouts split infrastructure into ~19 `core-*` modules. That's overkill for a template: it signals ceremony, pushes users toward premature abstraction, and fights the "read it end-to-end in an afternoon" goal.

We needed a shape that scales past a handful of features, keeps iOS consumption simple (Xcode links exactly one framework), and stays readable.

## Decision

Three Gradle modules, wired by two convention plugins in an included build (`build-logic/convention`):

- **`:core`** — shared infrastructure. `BaseViewModel`, `AppException`, `ErrorMapper`, `HttpClientFactory`, `Logger`, `Analytics`, `CrashReporter`, `KeyValueStore`, `SettingsFactory`, `CoreModule`, `DispatcherProvider`, `UiStatus`, `DesignTokens`, plus Android/iOS platform implementations. No feature code, no UI logic. Applies `template.kmp.library` and `api()`s the cross-cutting libraries (coroutines, Ktor, Koin, Kermit, Settings, lifecycle-viewmodel) so every downstream feature gets them for free.
- **`:feature:<name>`** — one feature per module, holding its own `domain/`, `data/`, and `presentation/` (ViewModels, State, SideEffect, Fakes). Applies `template.kmp.feature`, which adds `api(project(":core"))` automatically and wires the standard `commonTest` deps (Turbine, coroutines-test, kotlin-test).
- **`:shared`** — iOS framework umbrella + cross-cutting navigation. Holds only `Destination`, `DeepLinkParser`, `KoinBridge`, `KoinInitializer`. Applies `template.kmp.library` + `kotlin-serialization` + SKIE. `api()`s `:core` and every `:feature:*`, and `export()`s them in the iOS framework block so Swift sees a single `Shared.framework`.

## Why not mono-module

Works until ~15 features. Then every small edit triggers a full `shared` recompile, IDE responsiveness on indexing degrades, and the lack of module boundaries means anyone can import anything — feature A quietly reaches into feature B's internals, tests couple across features, and cleanup becomes a multi-PR project.

## Why not 19 `core-*` modules

NowInAndroid's split is great for a large app team with real ownership boundaries. For a template, it's a maintenance tax (19 `build.gradle.kts` files to keep aligned) and a bad first impression — it reads as "you'll need all this ceremony," which scares users off before they write their first feature.

## Why `:core` exists separately from `:shared`

Gradle forbids project cycles. `:shared` needs to re-export each `:feature:*` so Xcode can consume `Shared.framework` as a single artifact; `:feature:*` needs `BaseViewModel`, `AppException`, `HttpClientFactory`, etc. If infrastructure lived in `:shared`, the graph would be `:shared → :feature:* → :shared` — a cycle.

Extracting `:core` breaks it: `:feature:* → :core`, `:shared → :core + :feature:*`. `:shared` keeps only the types that genuinely belong to the app-wide composition root — navigation types and the iOS DI bridge — and nothing else.

## Consequences

**Positive.**

- Each feature compiles independently. Touching `feature/todo` doesn't rebuild `feature/profile`.
- `:shared` stays thin — navigation + iOS glue, nothing else. Easy to read, easy to diff.
- Adding a feature is mechanical: `cp -R feature/todo feature/<new>`, edit namespace and contents, add one line to `settings.gradle.kts`, one `api()` line and one `export()` line in `shared/build.gradle.kts`.
- Convention plugins collapse per-module Gradle config to a few lines — new `feature/*/build.gradle.kts` is five lines.
- Compiler-enforced feature isolation: feature A can't see feature B's internals unless something is explicitly `public` in a dependency.

**Negative.**

- More `build.gradle.kts` files (one per module). Mitigated by the convention plugins.
- `:shared` has to `api()` every feature so the iOS framework exports them. That's the cost of a monolithic `Shared.framework`; see alternatives below.

## Alternatives considered

- **Mono-module.** Rejected — doesn't scale, see above.
- **Multi-framework iOS** (one `.framework` per feature, Xcode embeds N). Rejected — complicates Xcode setup, increases binary size, and forces feature-aware import statements in Swift. We prefer one `import Shared` regardless of how many features exist.
- **TryTemplateApp's `core/core-base/core-ui/...` split (19 modules).** Rejected — over-engineering for a template.

## Related

- See [`docs/01-architecture/modularization.md`](../01-architecture/modularization.md) for the layout in practice.
- See [`docs/03-adding-a-feature/`](../03-adding-a-feature/) for the mechanical recipe.
