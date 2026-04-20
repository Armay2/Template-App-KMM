package com.electra.template.android.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.electra.template.core.theme.ColorTokens

fun ColorTokens.toLightScheme() = lightColorScheme(
    primary = Color(primary), onPrimary = Color(onPrimary),
    background = Color(background), onBackground = Color(onBackground),
    surface = Color(surface), onSurface = Color(onSurface),
    error = Color(error), onError = Color(onError),
)

fun ColorTokens.toDarkScheme() = darkColorScheme(
    primary = Color(primary), onPrimary = Color(onPrimary),
    background = Color(background), onBackground = Color(onBackground),
    surface = Color(surface), onSurface = Color(onSurface),
    error = Color(error), onError = Color(onError),
)
