package me.buddha.chiplesspoker.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import me.buddha.chiplesspoker.R

// Set of Material typography styles to start with

val CPFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.black_han_sans_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        )
    )
)

object CPTextStyle {
    val brandLarge = TextStyle(
        fontFamily = CPFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.5.sp
    )

    val brandSmall = TextStyle(
        fontFamily = CPFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.5.sp
    )
}

val LocalTextStyle = staticCompositionLocalOf { CPTextStyle }