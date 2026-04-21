# Design tokens

After reading this you'll know how the shared token set is structured and how each platform consumes it.

## Shared source of truth

`shared/src/commonMain/kotlin/com/electra/template/core/theme/DesignTokens.kt` is a plain data class composing four sub-sets:

```kotlin
data class DesignTokens(
    val light: ColorTokens = ColorTokens.Light,
    val dark:  ColorTokens = ColorTokens.Dark,
    val typography: TypographyTokens = TypographyTokens.Default,
    val spacing:    SpacingTokens    = SpacingTokens.Default,
) {
    companion object { val Default = DesignTokens() }
}
```

Each sub-set uses primitives (`Long` ARGB for colors, `Int` sp/dp) so it serialises naturally across the K/N boundary without needing Android `androidx.compose.ui.graphics.Color` in `commonMain`. See `ColorTokens.kt`, `SpacingTokens.kt`, `TypographyTokens.kt`.

## DI

`coreModule` registers the default: `single { DesignTokens.Default }`. Swap it by providing your own module with `single { DesignTokens(light = ...) }` that overrides the binding — or override at test time via Koin's test rule.

## Android consumption

`androidApp/.../theme/AppTheme.kt` pulls tokens from Koin and builds a Material 3 `ColorScheme` / `Typography`:

```kotlin
@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val tokens: DesignTokens = koinInject()
    MaterialTheme(
        colorScheme = if (darkTheme) tokens.dark.toDarkScheme() else tokens.light.toLightScheme(),
        typography  = tokens.typography.toMaterialTypography(),
        content     = content,
    )
}
```

Conversion helpers live in `androidApp/.../theme/Color.kt` and `Type.kt`.

## iOS consumption

`iosApp/iosApp/iosApp/Presentation/Theme/AppTheme.swift` wraps the Kotlin instance and exposes SwiftUI-native values. `Color(argb:)` handles the Long → Color conversion. `AppTheme.default` reads `DesignTokens.companion.Default`. The theme is injected via the `appTheme` `EnvironmentValue` in `ThemeEnvironment.swift`.

## Extending

Add your token (say `val elevationsDp: List<Int>`) in a new `ElevationTokens.kt`, wire it into `DesignTokens`, and surface it in both platform theme wrappers. Because `DesignTokens` is a `data class`, existing callers keep working.
