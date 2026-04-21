# Analytics

After reading this you'll know the tracker interface and how to wire a real vendor.

## Interface

`shared/src/commonMain/kotlin/com/electra/template/core/analytics/AnalyticsTracker.kt`:

```kotlin
interface AnalyticsTracker {
    fun track(event: String, properties: Map<String, Any?> = emptyMap())
    fun identify(userId: String?, traits: Map<String, Any?> = emptyMap())
}
```

`NoopAnalyticsTracker` (same package) is the default, registered in `coreModule` as `single<AnalyticsTracker> { NoopAnalyticsTracker() }`. The template intentionally ships no vendor dependency — see ADR 0003 for the rationale on keeping the core neutral.

## Wiring Firebase / Amplitude / Mixpanel

Create an impl in platform code (typically Android-only because most SDKs are Java/ObjC-first), register it in a platform Koin module, and let Koin override the binding:

```kotlin
// androidApp/.../di/AndroidModule.kt
val androidModule = module {
    single<AnalyticsTracker> { FirebaseAnalyticsTracker(FirebaseAnalytics.getInstance(get())) }
}
```

For iOS, add an iOS-only Kotlin impl in `shared/src/iosMain/` (e.g. `IosAnalyticsTracker : AnalyticsTracker`) and bind it in `iosPlatformModule`. If the vendor SDK is Swift-only, wrap it on the Swift side and expose a thin Kotlin interface the Kotlin-side consumers use. Keep `NoopAnalyticsTracker` as the `commonMain` default so tests and previews never require a live SDK.

## Call sites

Inject `AnalyticsTracker` into any ViewModel or use-case that needs to record events, and call `track("todo_created", mapOf("has_description" to (desc.isNotBlank())))` inline. Because the interface lives in `commonMain`, shared tests can assert calls against a fake tracker.
