package com.electra.template.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.electra.template.core.theme.DesignTokens
import org.koin.compose.koinInject

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val tokens: DesignTokens = koinInject()
    MaterialTheme(
        colorScheme = if (darkTheme) tokens.dark.toDarkScheme() else tokens.light.toLightScheme(),
        typography = tokens.typography.toMaterialTypography(),
        content = content,
    )
}
