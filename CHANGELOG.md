# Changelog

All notable changes to this template will be documented here.
Format: [Keep a Changelog](https://keepachangelog.com/en/1.1.0/); versioning: [SemVer](https://semver.org/).

## [Unreleased]

### Added
- `build-logic/convention` Gradle included build with two plugins: `template.kmp.library` and `template.kmp.feature`.
- New `:core` module holding all shared infrastructure (BaseViewModel, AppException, HttpClientFactory, Logger, Analytics, CrashReporter, KeyValueStore, SettingsFactory, CoreModule, ErrorMapper, DispatcherProvider, UiStatus, DesignTokens + platform implementations).
- New `:feature:todo` module extracted from `:shared` — demonstrates the feature-based pattern.
- ADR-0007 documenting the 3-module architecture decision.
- `docs/01-architecture/modularization.md` describing the module hierarchy and the iOS framework umbrella pattern.
- Updated `docs/03-adding-a-feature/` recipe for the multi-module workflow.

### Changed
- `:shared` is now an iOS framework umbrella: keeps only navigation (`Destination`, `DeepLinkParser`) and iOS DI bridge (`KoinBridge`, `KoinInitializer`), `api()`s `:core` + `:feature:todo`, and `export()`s both in the iOS framework so Swift continues to see a single `Shared.framework`.
- Bumped Kotlin 2.1.0 → 2.2.10, AGP 8.7.2 → 8.11.2, KSP matched, Gradle wrapper 8.10.2 → 8.13, Koin 4.0 → 4.1, Kermit 2.0.4 → 2.0.6, multiplatform-settings 1.2.0 → 1.3.0, kotlinx-coroutines 1.9 → 1.10.2, kotlinx-serialization 1.7.3 → 1.8.1, kotlinx-datetime 0.6.1 → 0.6.2, detekt 1.23.7 → 1.23.8, Compose BOM, androidx.* to current stables, compileSdk/targetSdk 35 → 36.
- Ktor held at 3.1.3 (not 3.2.0) — see commit 62fb5b1 for D8/DEX incompatibility with minSdk=26.

### Internal
- Consolidation with two now-retired sibling templates (`TemplateAppKMM`, `TryTemplateApp`). Their strongest ideas (`build-logic/convention`, feature-based modularization, version-catalog plugin classpath) landed here; scripts/fastlane/Firebase automation were intentionally not imported.

## [0.2.0] - 2026-04-21
### Changed
- Todo list UX polish: active + done sections with collapsible Done, quick-add bottom sheet for creation, swipe-left delete, native haptics, animated transitions, illustrated empty state.
- Todo detail polish: more generous spacing, delete confirmation (alert on iOS, AlertDialog on Android), primary Save button full-width.
- `TodoListSideEffect.NavigateToDetail.id` tightened from `String?` to `String`. Creation now flows exclusively through the new `OpenQuickAdd` side-effect. `TodoListViewModel.onCreateNew` renamed to `onRequestQuickAdd`; new `onQuickAdd(title)` and `onToggleDoneSection()` methods added.
- Replaced Kotlin `expect/actual` with plain interfaces + platform classes injected via Koin. `SettingsFactory` is now an interface in `commonMain`; `AndroidSettingsFactory` / `IosSettingsFactory` implement it. `platformModule` split into `androidPlatformModule` / `iosPlatformModule`. Removed the `-Xexpect-actual-classes` compiler flag.

### Fixed
- Flaky `loadsTodosOnRefresh` test: now awaits a state predicate rather than `advanceUntilIdle`, since Ktor MockEngine runs on its own dispatcher.

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
