# First run

After reading this you'll have the shared module built and both apps running against the stub JSONPlaceholder API.

## Build the shared module

From the repo root:

```bash
./gradlew :shared:build
```

This produces the Android library (AAR) and the three Apple XCFrameworks (`iosX64`, `iosArm64`, `iosSimulatorArm64`) under `shared/build/`. SKIE post-processes the `Shared.framework` to expose Kotlin sealed hierarchies, flows and suspend functions as idiomatic Swift.

## Run the Android app

```bash
./gradlew :androidApp:installDebug
adb shell am start -n com.electra.template.android/.MainActivity
```

The app ID is `com.electra.template.android` (see `androidApp/build.gradle.kts`). The launcher activity is `MainActivity`, which hosts `AppNavHost` inside `AppTheme`.

## Run the iOS app

Open the Xcode project:

```bash
open iosApp/iosApp/iosApp.xcodeproj
```

Select the **iPhone 17** simulator (or any iOS 26 device) and press Run. Xcode's "Run Script" build phase invokes `:shared:embedAndSignAppleFrameworkForXcode`, so Kotlin changes are picked up automatically. The entry point is `iosAppApp.swift` — `KoinInitializer().doInit()` wires DI before `RootView` mounts the `NavigationStack`.

## Verify

Both apps should display a list titled **Todos** populated from `https://jsonplaceholder.typicode.com/todos` (see `TodoModule.kt`).

Next: `rename-the-template.md`.
