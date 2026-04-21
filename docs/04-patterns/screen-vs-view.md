# Screen vs. View

After reading this you'll know why every feature is split in two composables/views and where previews plug in.

## The split

Each feature has two files per platform:

- **Screen** — the container. Knows about DI, the `ViewModel`, side-effects, navigation. Not previewable because it depends on Koin and a real VM. Example: `TodoListScreen.kt` on Android and `TodoListScreen.swift` on iOS.
- **View** — the pure renderer. Takes `state` and an `Actions` struct (function references) and renders UI. No DI, no flows. Fully previewable and unit-testable. Example: `TodoListView.kt` on Android, `TodoListView.swift` on iOS.

## Why

- **Testability** — the View can be exercised with a Compose UI test (`TodoListViewTest.kt`) or an XCUITest using `*Fakes` data without standing up the whole DI graph.
- **Previews** — `@Preview` on Android and `#Preview` on iOS both take `*Fakes` instances. Designer review and Xcode/Android Studio previews work without a running backend.
- **Reusability of fakes across layers** — `shared/.../TodoListFakes.kt` is a shared object consumed by both platforms *and* by Compose / SwiftUI previews, so the "empty / loading / with-items / error" variants stay in one place:

```kotlin
object TodoListFakes {
    val Empty     = TodoListState()
    val Loading   = TodoListState(status = UiStatus.Loading)
    val WithItems = TodoListState(todos = listOf(Todo("1", "Buy groceries", "", false)), status = UiStatus.Idle)
    val Error     = TodoListState(status = UiStatus.Error("Network unavailable", retryable = true))
}
```

## Actions pattern

Both platforms wrap the callbacks in a small data type (`TodoListActions`) so the pure View signature stays readable and previewable with `.noop` / `noOpActions()` — see `TodoListView.swift` for `.noop` and `TodoListView.kt` for `noOpActions()`.
