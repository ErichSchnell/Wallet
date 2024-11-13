package com.example.wallet.presentation.util.ex

import androidx.compose.ui.graphics.Color

fun Color.adjustBrightness(factor: Float): Color {
    val red = (red * factor).coerceIn(0f, 1f)
    val green = (green * factor).coerceIn(0f, 1f)
    val blue = (blue * factor).coerceIn(0f, 1f)
    return Color(red, green, blue, alpha)
}

fun Color.toStringHex(): String{
    val color = this

    val red = (color.red * 255).toInt()
    val green = (color.green * 255).toInt()
    val blue = (color.blue * 255).toInt()

    val colorHex = String.format("#%02X%02X%02X", red, green, blue)
    return colorHex
}