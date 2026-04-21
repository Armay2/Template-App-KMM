# Adding a feature — PR checklist

After reading this you'll have the minimum checklist to paste into a PR description.

- [ ] Domain: entity + repo interface in `shared/.../domain/<feature>/`. Suspend methods annotated `@Throws(AppException::class, CancellationException::class)`.
- [ ] Data: DTO (`@Serializable`), API (Ktor) with `wrap { }.toAppException()`, mapper, repo impl with in-memory cache via `MutableStateFlow`.
- [ ] Use cases in `domain/.../usecase/` — only when they add validation or orchestration on top of the repo.
- [ ] Koin module registered in `TemplateApplication.kt` (Android) **and** `KoinInitializer.kt` (iOS).
- [ ] `State`, `SideEffect`, `ViewModel` extending `BaseViewModel` in `presentation/<feature>/`.
- [ ] `*Fakes.kt` covering empty / loading / populated / error. Shared between previews and tests.
- [ ] `Destination` extended in `core/navigation/Destination.kt` (+ `DeepLinkParser` if the feature is deep-linkable).
- [ ] Android navigation: `Routes` + `Destination.toRoute()` + `composable(...)` in `AppNavHost.kt`.
- [ ] iOS navigation: `Route` enum + converter + `DestinationRouter.swift` case.
- [ ] Screen container + pure View on each platform, with `@Preview` / `#Preview` using `*Fakes`.
- [ ] Tests: repo (Ktor `MockEngine`), VM (Turbine + `runTest`), UI smoke (Compose UI / XCUITest).
- [ ] `CHANGELOG.md` updated under `Unreleased`.
- [ ] `./gradlew :shared:allTests :androidApp:assembleDebug` pass. Xcode builds iOS clean in Swift 6 strict.
