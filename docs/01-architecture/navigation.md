# Navigation

After reading this you'll understand why the destination model lives in shared code while rendering is native, and where the translation happens on each platform.

## Shared `Destination`

`shared/src/commonMain/kotlin/com/electra/template/core/navigation/Destination.kt` defines a sealed interface that any shared code (ViewModels, deep-link parser) can emit:

```kotlin
sealed interface Destination {
    data object TodoList : Destination
    data class TodoDetail(val id: String?) : Destination // null means "new"
}
```

`DeepLinkParser.parse("template://todos/42")` returns a `Destination` ready to be pushed by either platform. There is one `DeepLinkParserTest` in `commonTest` that locks the URL grammar.

## Android rendering

`androidApp/src/main/kotlin/com/electra/template/android/navigation/DestinationMapping.kt` translates `Destination` to Compose Navigation route strings:

```kotlin
fun Destination.toRoute(): String = when (this) {
    is Destination.TodoList   -> Routes.TodoList
    is Destination.TodoDetail -> Routes.todoDetail(id)
}
```

`AppNavHost.kt` declares the `NavHost` with `composable(Routes.TodoList) { ... }` and a `{id}` arg for detail. Screens call `onNavigate(Destination.X)` and the host invokes `nav.navigate(dest.toRoute())`.

## iOS rendering

SwiftUI's `NavigationStack` needs a `Hashable` path element, which K/N-generated `Destination` protocol isn't. `iosApp/iosApp/iosApp/Presentation/Navigation/AppNavigator.swift` defines a native `enum Route: Hashable` mirroring the sealed interface with a converter `init?(from destination: any Destination)`. `DestinationRouter.swift` installs `navigationDestination(for: Route.self)` via a view modifier — screens listen for VM side-effects and call `navigator.push(.todoDetail(id:))`.

This keeps navigation **type-safe per platform** while letting shared code (or deep links) dispatch in `Destination` currency.
