package com.electra.template.core.theme

data class DesignTokens(
    val light: ColorTokens = ColorTokens.Light,
    val dark: ColorTokens = ColorTokens.Dark,
    val typography: TypographyTokens = TypographyTokens.Default,
    val spacing: SpacingTokens = SpacingTokens.Default,
) {
    companion object {
        val Default = DesignTokens()
    }
}
