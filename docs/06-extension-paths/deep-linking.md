# Deep linking

After reading this you'll know where to extend URL parsing and how each platform feeds a URL into the shared router.

## Shared parser

`shared/src/commonMain/kotlin/com/electra/template/core/navigation/DeepLinkParser.kt` already maps the `template://` scheme:

```kotlin
object DeepLinkParser {
    private const val SCHEME = "template://"
    fun parse(url: String): Destination? {
        if (!url.startsWith(SCHEME)) return null
        val path = url.removePrefix(SCHEME).trim('/').split('/')
        return when {
            path == listOf("todos") -> Destination.TodoList
            path.size == 2 && path[0] == "todos" && path[1] == "new" -> Destination.TodoDetail(null)
            path.size == 2 && path[0] == "todos" -> Destination.TodoDetail(path[1])
            else -> null
        }
    }
}
```

To support a new feature, add a branch and a test case in `DeepLinkParserTest.kt`. Keep the parser pure and exhaustive — it's your routing contract.

## Android — intent-filter

`androidApp/src/main/AndroidManifest.xml` already declares the `template` scheme:

```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="template" />
</intent-filter>
```

In `MainActivity.onCreate`, read `intent?.data?.toString()`, call `DeepLinkParser.parse(url)`, and push the resulting `Destination` into `AppNavHost` via `nav.navigate(dest.toRoute())`. Handle follow-up intents in `onNewIntent`.

## iOS — onOpenURL

Add `CFBundleURLTypes` in the iOS `Info.plist` for the `template` scheme, then handle the URL with SwiftUI's `.onOpenURL`:

```swift
RootView()
    .onOpenURL { url in
        if let dest = DeepLinkParser.shared.parse(url: url.absoluteString) {
            navigator.push(dest)
        }
    }
```

`AppNavigator.push(_ destination: any Destination)` already knows how to map a Kotlin `Destination` to the local `Route` enum (see `Presentation/Navigation/AppNavigator.swift`).

## Universal links / App Links

Adding HTTPS universal links is out of scope for the template — you'll need an associated-domains entitlement on iOS and `autoVerify="true"` plus an `assetlinks.json` on Android. The shared parser stays the same; only the platform entry points change.
