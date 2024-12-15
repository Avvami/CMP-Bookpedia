package com.plcoding.bookpedia.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    secondary = secondaryLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceContainer = surfaceContainerLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    secondary = secondaryDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceContainer = surfaceContainerDark,
)

@Composable
fun BookiesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

