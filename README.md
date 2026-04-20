# Template-App-KMM

Production-ready Kotlin Multiplatform Mobile template for starting any new Android + iOS app.

Shared Kotlin code goes up to the ViewModel layer. UIs stay native: Jetpack Compose on Android, SwiftUI on iOS 26+.

## Prerequisites

- JDK 21
- Android Studio Ladybug+ / IntelliJ 2024.3+
- Xcode 26+ (iOS 26 SDK, macOS 15+)
- Kotlin 2.1+
- Gradle (via wrapper)

## Quick start

```bash
git clone <this-repo> my-new-app && cd my-new-app
./gradlew :shared:build
./gradlew :androidApp:installDebug
open iosApp/iosApp.xcodeproj
```

## Documentation

See `docs/` — architecture, patterns, extension paths, ADRs.
