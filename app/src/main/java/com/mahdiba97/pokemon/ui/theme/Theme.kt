package com.mahdiba97.pokemon.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = yellow500,
    primaryVariant = yellow700,
    secondary = blue200,
    onBackground = Color.White,
    background = Color(0xff101010),
    surface = Color(0xff303030),
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = blue500,
    primaryVariant = yellow700,
    secondary = blue200,
    onBackground = Color.Black,
    background = Color.Cyan,
    surface = Color.White,
    onSurface = Color.Black

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun PokemonTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}