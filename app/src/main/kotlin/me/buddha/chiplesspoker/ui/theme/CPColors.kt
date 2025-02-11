package me.buddha.chiplesspoker.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

object CPColors {
    val color_081452 = Color(0xFF081452)
    val color_751ABD = Color(0xFF751ABD)
    val color_FFFFFF = Color(0xFFFFFFFF)
    val color_1C1B14 = Color(0xFF1C1B14)
    val color_3A3A3A = Color(0xFF3A3A3A)
}

data class CustomColors(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val brandColor: Color,
    val negativeColor: Color,
    val positiveColor: Color,
)

val LightColorScheme = CustomColors(
    primary = CPColors.color_FFFFFF,
    secondary = CPColors.color_1C1B14,
    background = CPColors.color_3A3A3A,
    brandColor = CPColors.color_751ABD,
    negativeColor = CPColors.color_1C1B14,
    positiveColor = CPColors.color_FFFFFF
)

val DarkColorScheme = LightColorScheme

val LocalThemeColors = staticCompositionLocalOf<CustomColors> { LightColorScheme }