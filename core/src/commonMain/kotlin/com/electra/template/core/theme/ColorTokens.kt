package com.electra.template.core.theme

data class ColorTokens(
    val primary: Long,
    val onPrimary: Long,
    val background: Long,
    val onBackground: Long,
    val surface: Long,
    val onSurface: Long,
    val error: Long,
    val onError: Long,
) {
    companion object {
        val Light =
            ColorTokens(
                primary = 0xFF2E5AAC,
                onPrimary = 0xFFFFFFFF,
                background = 0xFFFDFDFF,
                onBackground = 0xFF1A1B1F,
                surface = 0xFFFFFFFF,
                onSurface = 0xFF1A1B1F,
                error = 0xFFBA1A1A,
                onError = 0xFFFFFFFF,
            )
        val Dark =
            ColorTokens(
                primary = 0xFFB1C5FF,
                onPrimary = 0xFF002B6F,
                background = 0xFF1A1B1F,
                onBackground = 0xFFE3E2E6,
                surface = 0xFF1A1B1F,
                onSurface = 0xFFE3E2E6,
                error = 0xFFFFB4AB,
                onError = 0xFF690005,
            )
    }
}
