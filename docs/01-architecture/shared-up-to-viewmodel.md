# Shared up to the ViewModel

After reading this you'll understand what lives in `shared` and what must stay native, with the reasoning behind the cut.

## What is shared

- **Entities** (`domain/todo/Todo.kt`): plain data classes, no framework deps.
- **Repositories** (`domain/todo/TodoRepository.kt` interface + `data/todo/TodoRepositoryImpl.kt`): remote (Ktor), cache (`MutableStateFlow`), Settings.
- **Use cases** (`domain/todo/usecase/*`): thin orchestration, sometimes with validation (`CreateTodoUseCase` rejects blank titles).
- **ViewModels** (`presentation/base/BaseViewModel.kt` + feature VMs): they own `StateFlow<State>` and `SharedFlow<SideEffect>`. They use the JetBrains KMP fork of `androidx.lifecycle` (`libs.jb.lifecycle.viewmodel`) so `viewModelScope` is available in `commonMain`.
- **State / side-effect types** (`TodoListState.kt`, `TodoListSideEffect.kt`): Kotlin `data class`/`sealed interface`, exposed to Swift via SKIE.
- **Previews and test fixtures** (`*Fakes.kt`): a single source of truth for both Compose `@Preview` and SwiftUI `#Preview`.
- **Design tokens, navigation destinations, deep-link parser, error types, observability interfaces.**

## What stays native

- Any `@Composable` or `View` — rendered with Compose / Material 3 on Android and SwiftUI / Liquid Glass on iOS.
- Navigation host — `NavHost` on Android, `NavigationStack` on iOS.
- DI host (Koin start-up): `TemplateApplication.kt` on Android, `KoinInitializer().doInit()` on iOS.
- Platform-specific implementations behind `expect/actual` (`SettingsFactory`, `platformModule`).

## Why this line

Everything below the ViewModel is platform-agnostic data orchestration and is worth the share cost. Above it, users feel platform inconsistencies immediately, and native toolkits have years of compounding polish (gesture systems, accessibility, animations, system integrations) we don't want to reimplement. Keeping the cut at the VM means state, validation, and side-effect orchestration are covered by a single test suite in `commonTest`.
