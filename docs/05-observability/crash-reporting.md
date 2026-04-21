# Crash reporting

After reading this you'll know the crash reporter interface and where to wire Crashlytics or Sentry.

## Interface

`shared/src/commonMain/kotlin/com/electra/template/core/crash/CrashReporter.kt`:

```kotlin
interface CrashReporter {
    fun setUser(userId: String?)
    fun log(breadcrumb: String)
    fun recordException(throwable: Throwable)
}
```

`NoopCrashReporter` is the default, registered in `coreModule`. No vendor SDK ships with the template (ADR 0003).

## Wiring Crashlytics

Android-only, because Firebase's SDK is JVM-native. In `androidApp/src/main/kotlin/com/electra/template/android/di/AndroidModule.kt`:

```kotlin
val androidModule = module {
    single<CrashReporter> {
        object : CrashReporter {
            private val c = FirebaseCrashlytics.getInstance()
            override fun setUser(userId: String?) { c.setUserId(userId ?: "") }
            override fun log(breadcrumb: String) { c.log(breadcrumb) }
            override fun recordException(throwable: Throwable) { c.recordException(throwable) }
        }
    }
}
```

The placeholder comment in `AndroidModule.kt` already flags this as the intended integration point.

## Wiring Sentry

Sentry ships a Kotlin Multiplatform SDK; you can declare a single impl in `commonMain` (or `iosMain` + `androidMain` if it requires platform init) and expose it via `coreModule` or a dedicated module. Same override pattern as analytics: last-registered binding wins.

Hook `recordException` into the catch blocks of your ViewModels (e.g. in the `AppException.Unknown` branch) or wrap `ErrorMapper.map` to log-and-rethrow.
