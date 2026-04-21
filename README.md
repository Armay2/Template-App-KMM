# Template-App-KMM

A production-ready **Kotlin Multiplatform Mobile** template for starting any new Android + iOS app, with a clear architectural line: **shared Kotlin up to the ViewModel**, **native UIs** on both sides (Jetpack Compose on Android, SwiftUI on iOS 26+).

Use this as the starting point of a greenfield app, or as a reference for how to wire KMP + Koin + SKIE + Ktor with a generic iOS `Observable` bridge and no per-feature platform glue.

> Status: `v0.1.0` — stable enough to build real features on top. See [`CHANGELOG.md`](CHANGELOG.md).

## A note from the author

I built this as a **lightweight starting point** for any kind of KMM app — something opinionated enough to be useful on day one, but small enough that you can read it end-to-end in an afternoon.

It also reflects **my own vision** of how a KMP codebase should be wired: a firm line between shared logic and native UI, a single generic iOS bridge, plain interfaces + DI instead of `expect`/`actual` for platform work, and vendor-neutral observability by default.

This is a **v1 architecture**, not a final one. It will naturally need to evolve for larger projects — typically around persistence (adding SQLDelight or Room), modularization (splitting `shared` into feature modules), authentication, offline-first strategies, or deeper navigation graphs. The [`docs/06-extension-paths/`](docs/06-extension-paths/) folder is where I've sketched those natural growth directions.

## Why this template

- **Native UI, shared logic.** The shared module stops at the ViewModel. UIs stay idiomatic on each platform — no Compose Multiplatform, no SwiftUI-over-Kotlin.
- **One generic iOS bridge.** A single `Observed<State, Effect, VM>` wrapper adapts any shared ViewModel to SwiftUI. No per-feature adapters to write.
- **DI without `expect`/`actual` ceremony.** Plain Kotlin interfaces in `commonMain`, platform implementations injected via Koin ([ADR-0002](docs/07-adr/0002-koin-for-di.md)).
- **Vendor-neutral observability.** `Logger`, `AnalyticsTracker`, `CrashReporter` are abstractions — no-op by default, easy to wire to Firebase / Sentry / DataDog later.
- **Shared design tokens.** Typography, spacing, and color tokens defined once in Kotlin; consumed by Material 3 on Android and a SwiftUI `AppTheme` on iOS 26.
- **Tooling on by default.** detekt, ktlint, SwiftLint, SwiftFormat, Lefthook pre-commit hooks, and three CI workflows (shared / Android / iOS) out of the box.

## Stack

| Layer | Tool |
|------|------|
| Shared lang | Kotlin 2.1 |
| DI | Koin 4 (`koin-core-viewmodel`) |
| HTTP | Ktor (MockEngine-tested) |
| Kotlin↔Swift interop | SKIE 0.10.11 |
| Persistence | Multiplatform Settings (KV only — see [ADR-0003](docs/07-adr/0003-settings-only-no-db.md)) |
| Logging | Kermit |
| Android UI | Jetpack Compose, Material 3, Compose Navigation |
| iOS UI | SwiftUI, `NavigationStack`, iOS 26 SDK, Swift 6 strict concurrency |
| Quality | detekt, ktlint, SwiftLint, SwiftFormat, Lefthook |
| CI | GitHub Actions (Xcode 26, macos-15) |

## Prerequisites

- JDK 21
- Android Studio Ladybug+ / IntelliJ 2024.3+
- Xcode 26+ (iOS 26 SDK, macOS 15+)
- Kotlin 2.1+
- Gradle (via wrapper)

See [`docs/00-getting-started/prerequisites.md`](docs/00-getting-started/prerequisites.md) for details.

## Quick start

```bash
# Use this template from GitHub (preferred), or:
git clone https://github.com/<your-user>/Template-App-KMM.git my-new-app
cd my-new-app

# Build & test the shared module
./gradlew :shared:build

# Run Android
./gradlew :androidApp:installDebug

# Run iOS
open iosApp/iosApp.xcodeproj
# then ⌘R in Xcode
```

Then rename the template to your app: [`docs/00-getting-started/rename-the-template.md`](docs/00-getting-started/rename-the-template.md).

## Project structure

```
.
├── shared/                  # Kotlin Multiplatform module (domain, data, presentation up to VM)
│   └── src/
│       ├── commonMain/      # Platform-agnostic code
│       ├── androidMain/     # Android-specific bindings
│       ├── iosMain/         # iOS-specific bindings
│       └── commonTest/      # Shared tests (Ktor MockEngine, VM tests)
├── androidApp/              # Jetpack Compose app, Material 3 theme derived from shared tokens
├── iosApp/                  # SwiftUI app (iOS 26), generic Observed wrapper for all VMs
└── docs/                    # Architecture, patterns, ADRs, extension paths
```

## Documentation

The `docs/` tree is organized as a learning path:

- [`00-getting-started/`](docs/00-getting-started/) — prerequisites, first run, renaming the template
- [`01-architecture/`](docs/01-architecture/) — overview, data flow, navigation, error handling
- [`02-platform-bridges/`](docs/02-platform-bridges/) — Koin on Android, the iOS `Observed` bridge, SKIE config
- [`03-adding-a-feature/`](docs/03-adding-a-feature/) — step-by-step recipe + PR checklist
- [`04-patterns/`](docs/04-patterns/) — ViewModel, use-cases, repositories, testing, theming, screen vs view
- [`05-observability/`](docs/05-observability/) — logging, analytics, crash reporting
- [`06-extension-paths/`](docs/06-extension-paths/) — auth, persistence (SQLDelight/Room), deep linking, offline-first, pagination
- [`07-adr/`](docs/07-adr/) — architecture decision records

## Contributing

See [`CONTRIBUTING.md`](CONTRIBUTING.md). The repo uses Conventional Commits and enforces quality checks via Lefthook.

## License

[MIT](LICENSE) — do whatever you want, just keep the copyright notice.
