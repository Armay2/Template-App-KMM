import SwiftUI
import Shared

/// SwiftUI-facing projection of the shared `DesignTokens`.
///
/// Wraps a Kotlin `DesignTokens` instance and exposes platform-native
/// `Color` / `CGFloat` values. `AppTheme.default` pulls the companion
/// defaults from the shared module.
struct AppTheme {
    let tokens: DesignTokens

    var primary: Color { Color(argb: tokens.light.primary) }
    var background: Color { Color(argb: tokens.light.background) }
    var onBackground: Color { Color(argb: tokens.light.onBackground) }
    var spacingM: CGFloat { CGFloat(truncating: tokens.spacing.mDp as NSNumber) }

    static let `default` = AppTheme(tokens: DesignTokens.companion.Default)
}

extension Color {
    /// Initialize a SwiftUI `Color` from an ARGB-packed 64-bit integer
    /// (the format used by our Kotlin `ColorTokens`).
    init(argb: Int64) {
        let a = Double((argb >> 24) & 0xFF) / 255
        let r = Double((argb >> 16) & 0xFF) / 255
        let g = Double((argb >> 8) & 0xFF) / 255
        let b = Double(argb & 0xFF) / 255
        self.init(.sRGB, red: r, green: g, blue: b, opacity: a)
    }
}
