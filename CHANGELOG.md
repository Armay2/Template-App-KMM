# Changelog

All notable changes to this template will be documented here.
Format: [Keep a Changelog](https://keepachangelog.com/en/1.1.0/); versioning: [SemVer](https://semver.org/).

## [Unreleased]

## [0.1.0] - 2026-04-21
### Added
- KMM monorepo with `shared`, `androidApp`, `iosApp` modules.
- Todo example demonstrating all template patterns (list + detail + CRUD, in-memory cache + remote via Ktor MockEngine).
- Generic iOS `Observed<State, Effect, VM>` wrapper — no per-feature adapters.
- Shared `Destination` sealed interface with native Compose Navigation (Android) and SwiftUI `NavigationStack` + `Route` enum (iOS).
- Observability abstractions: `Logger` (Kermit), `AnalyticsTracker`, `CrashReporter` — all no-op by default, vendor-neutral.
- Shared `DesignTokens` consumed by Material 3 on Android and SwiftUI `AppTheme` on iOS 26.
- CI workflows for shared, Android, and iOS (GitHub Actions, Xcode 26, macos-15).
- Lefthook pre-commit hooks; detekt, ktlint, SwiftLint, SwiftFormat configs.
- Documentation: getting-started, architecture, platform bridges, patterns, extension paths, and 6 ADRs under `docs/`.
- Swift 6 strict concurrency compatibility on iOS 26+ (isolated deinit in `Observed`).
- Koin 4 DI with KMP ViewModel support (`koin-core-viewmodel`), `viewModelScope` via the JetBrains KMP fork of `androidx.lifecycle.ViewModel`.
- SKIE 0.10.11 for Kotlin↔Swift interop (flow interop defaults, sealed enum generation).
