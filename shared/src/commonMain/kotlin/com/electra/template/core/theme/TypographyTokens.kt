package com.electra.template.core.theme

data class TypographyTokens(
    val displayLargeSp: Int = 57,
    val headlineMediumSp: Int = 28,
    val titleLargeSp: Int = 22,
    val bodyLargeSp: Int = 16,
    val labelLargeSp: Int = 14,
) {
    companion object {
        val Default = TypographyTokens()
    }
}
