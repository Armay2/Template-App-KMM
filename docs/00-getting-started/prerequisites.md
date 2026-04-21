# Prerequisites

After reading this you'll know the exact tool versions required to build and run the template on a fresh machine.

## Host

- macOS **15 (Sequoia) or newer** — required because iOS work needs Xcode 26.
- ~15 GB free disk space for Gradle caches, Xcode derived data, and Kotlin/Native konan artefacts.

## JVM

- **JDK 21** (LTS). The Gradle build targets `JavaVersion.VERSION_21` in both `shared/build.gradle.kts` and `androidApp/build.gradle.kts`, and Kotlin `jvmTarget = "21"`.

Check with `java -version`. Install via [Adoptium Temurin 21](https://adoptium.net) or `brew install --cask temurin21`.

## Kotlin / Gradle

- **Kotlin 2.1** (K2 compiler) — pinned in `gradle/libs.versions.toml` under `[versions] kotlin`.
- **Gradle** is provided via the wrapper (`./gradlew`). Do not install Gradle globally; use the wrapper to guarantee reproducible builds.
- **AGP 8.7+** (Android Gradle Plugin), managed by the version catalog.

## Android

- **compileSdk 35, targetSdk 35, minSdk 26**. Install SDK platform 35 via Android Studio Ladybug or `sdkmanager "platforms;android-35"`.

## iOS

- **Xcode 26 or newer** with the iOS 26 SDK. SKIE 0.10.11 in `libs.versions.toml` is aligned with Swift 6 strict concurrency.
- An iPhone 17 simulator (or any iOS 26 device) for running the iOS app.

## Tooling (optional but recommended)

- `lefthook` (see `lefthook.yml`) — pre-commit hooks for `ktlint`/`detekt`/SwiftFormat/SwiftLint.

See `first-run.md` next.
