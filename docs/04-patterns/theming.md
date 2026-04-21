# Theming

After reading this you'll know how light/dark flips work and which platform-specific design language applies.

## Light / dark

Tokens ship two `ColorTokens` sets (`Light`, `Dark`). Switching is platform-driven:

- **Android** — `AppTheme` reads `isSystemInDarkTheme()` and selects `tokens.dark.toDarkScheme()` or `tokens.light.toLightScheme()` (see `androidApp/.../theme/AppTheme.kt`). Supplying your own `darkTheme` override lets you force a mode for tests.
- **iOS** — SwiftUI automatically re-renders when the `colorScheme` environment changes. The `AppTheme` struct (`iosApp/.../Presentation/Theme/AppTheme.swift`) currently reads `tokens.light.*`. Extend it to read `tokens.dark.*` based on `@Environment(\.colorScheme)` if you want per-mode values.

Colors are stored as `Long` ARGB in the shared `ColorTokens`:

```kotlin
val Light = ColorTokens(
    primary = 0xFF2E5AAC, onPrimary = 0xFFFFFFFF,
    background = 0xFFFDFDFF, onBackground = 0xFF1A1B1F,
    ...
)
```

## Android — Material 3 Expressive

`androidApp/build.gradle.kts` pulls `compose.bom` and `compose.material3`. `toLightScheme()` / `toDarkScheme()` produce Material 3 `ColorScheme` instances, and the typography helper produces a `Typography` with the standard Material 3 slots (`displayLarge`, `headlineMedium`, `titleLarge`, `bodyLarge`, `labelLarge`). Components like `Scaffold`, `TopAppBar`, `ExtendedFloatingActionButton` in `TodoListView.kt` pick up the scheme via `MaterialTheme`.

## iOS — Liquid Glass / iOS 26

Because the target is iOS 26+, SwiftUI components already adopt the Liquid Glass aesthetic when placed inside `NavigationStack` (translucent toolbars, hair-fine dividers). `TodoListView.swift` uses idiomatic `List` / `ContentUnavailableView("No todos yet.", systemImage: "checklist")` — no custom chrome required.

No `@available(iOS <26, *)` fallbacks exist in the template; the design spec locks the min target, so you can use Swift 6 and Observation framework features unconditionally (see `Observed.swift`).
