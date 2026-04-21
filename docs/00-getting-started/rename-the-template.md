# Rename the template

After reading this you'll know every file and identifier that carries the `electra/template` name and how to change it in one pass.

## Package / namespace

The Kotlin base package is `com.electra.template`. Replace it throughout:

- `shared/src/commonMain/kotlin/com/electra/template/**` — move the folder tree.
- `shared/build.gradle.kts` → `android.namespace = "com.electra.template.shared"`.
- `androidApp/build.gradle.kts` → `android.namespace` and `defaultConfig.applicationId` (currently `com.electra.template.android`).
- Every Kotlin file `package` declaration and `import` statement.

## iOS bundle identifier

- `iosApp/iosApp/iosApp.xcodeproj/project.pbxproj` → `PRODUCT_BUNDLE_IDENTIFIER` (search & replace).
- `iosApp/iosApp/iosApp/iosAppApp.swift` struct name if you want to rename the SwiftUI `App` type.

## App name and deep-link scheme

- `androidApp/src/main/res/values/strings.xml` → `app_name`.
- `androidApp/src/main/AndroidManifest.xml` → the `<data android:scheme="template" />` intent-filter. Update the same scheme inside `DeepLinkParser.kt` (`private const val SCHEME = "template://"`).
- iOS `Info.plist` URL types if/when you add deep-link handling.

## Git / CI / changelog

- Update `README.md`, `CONTRIBUTING.md`, and `CHANGELOG.md` — reset the changelog to an empty `Unreleased` section; the template history is not your project's history.
- Point your `git remote set-url origin <your-new-repo>`.
- `.github/workflows/*.yml` — rename the workflow `name` fields; the jobs themselves are generic.

## Sanity check

```bash
./gradlew :shared:build :androidApp:assembleDebug
xcodebuild -project iosApp/iosApp/iosApp.xcodeproj -scheme iosApp build
```

Both must pass before you push the first commit to the new remote.
