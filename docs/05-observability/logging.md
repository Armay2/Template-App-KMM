# Logging

After reading this you'll know the logger interface and how to swap the default implementation.

## Interface

`shared/src/commonMain/kotlin/com/electra/template/core/logging/Logger.kt` defines a minimal leveled API:

```kotlin
interface Logger {
    fun d(tag: String, message: String, throwable: Throwable? = null)
    fun i(tag: String, message: String, throwable: Throwable? = null)
    fun w(tag: String, message: String, throwable: Throwable? = null)
    fun e(tag: String, message: String, throwable: Throwable? = null)
}
```

## Default impl

`KermitLogger` in the same file wraps [Kermit](https://github.com/touchlab/Kermit), which already routes to `Log` on Android and `OSLog` on Apple platforms. It's registered in `coreModule` as `single<Logger> { KermitLogger() }`.

## Swapping

In your own Koin module (e.g. `androidModule.kt` or an equivalent on iOS), declare:

```kotlin
single<Logger> { MyCustomLogger(get()) }
```

Koin's last-in-wins binding will override the default. You can also point Kermit at additional sinks (Crashlytics, Datadog) by configuring Kermit itself before `KermitLogger` is constructed — see the Kermit docs for `Logger.setLogWriters(...)`.
