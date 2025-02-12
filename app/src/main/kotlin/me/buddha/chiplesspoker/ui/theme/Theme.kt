package me.buddha.chiplesspoker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

@Composable
fun ChiplessPokerTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colors = remember(darkTheme) {
    if (darkTheme) DarkColorScheme else LightColorScheme
  }

  val textStyle = CPTextStyle

  CompositionLocalProvider(
    LocalThemeColors provides colors,
    LocalTextStyle provides textStyle
  ) {
    content()
  }
}