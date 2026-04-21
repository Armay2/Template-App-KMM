package com.electra.template.android.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.electra.template.core.theme.TypographyTokens

fun TypographyTokens.toMaterialTypography() =
    Typography(
        displayLarge = TextStyle(fontSize = displayLargeSp.sp),
        headlineMedium = TextStyle(fontSize = headlineMediumSp.sp),
        titleLarge = TextStyle(fontSize = titleLargeSp.sp),
        bodyLarge = TextStyle(fontSize = bodyLargeSp.sp),
        labelLarge = TextStyle(fontSize = labelLargeSp.sp),
    )
