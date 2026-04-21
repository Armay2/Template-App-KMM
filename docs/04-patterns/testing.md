# Testing

After reading this you'll know which test pyramid each layer gets and where its fixtures live.

## Shared — kotlin.test + Turbine + Ktor MockEngine

`shared/src/commonTest/kotlin/` holds commonTest sources. The toolkit, pinned in `shared/build.gradle.kts`:

- `kotlin("test")` — `@Test`, `assertEquals`, `assertIs`.
- `kotlinx-coroutines-test` — `StandardTestDispatcher`, `runTest`, `Dispatchers.setMain`.
- `app.cash.turbine:turbine` — assertions on `SharedFlow` / `Flow`.
- `io.ktor:ktor-client-mock` — fake HTTP (used in `FakeTodoApi.kt`).

Example ViewModel test (`TodoListViewModelTest.kt`):

```kotlin
@Test
fun emitsNavigateToDetailOnSelect() = runTest(dispatcher) {
    val vm = vm(emptyList())
    vm.effects.test {
        vm.onSelect("42")
        assertEquals(TodoListSideEffect.NavigateToDetail("42"), awaitItem())
    }
}
```

Repository tests (`TodoRepositoryImplTest.kt`) exercise `MutableStateFlow` cache transitions with Turbine and a `FakeTodoApi` built on `MockEngine`. Parser tests (`DeepLinkParserTest.kt`) are plain data-driven asserts.

## Android — Compose UI test

`androidApp/src/androidTest/` uses `androidx.compose.ui.test.junit4`. `TodoListViewTest.kt`:

```kotlin
@Test
fun displaysItemsWhenStateHasTodos() {
    compose.setContent {
        TodoListView(TodoListFakes.WithItems, TodoListActions({}, {}, {}, {}))
    }
    compose.onNodeWithText("Buy groceries").assertIsDisplayed()
}
```

Reuses `TodoListFakes` from `commonMain` so no Android-specific fixtures are needed.

## iOS — XCUITest smoke

`iosApp/iosApp/iosAppUITests/TodoFlowUITests.swift` launches the app and checks a navigation bar is present. We use XCUITest rather than Swift Testing for UI because XCTest has the simulator-level `XCUIApplication` APIs. Unit-level iOS logic, when it exists, uses Swift Testing (`iosAppTests/iosAppTests.swift`).

## Running

```bash
./gradlew :shared:allTests :androidApp:connectedDebugAndroidTest
xcodebuild -project iosApp/iosApp/iosApp.xcodeproj -scheme iosApp -destination 'platform=iOS Simulator,name=iPhone 17' test
```
