package com.ssemaj.imdbapp.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

enum class WindowType { Compact, Medium, Expanded }

@Composable
fun rememberWindowSize(): WindowType {
    val config = LocalConfiguration.current
    return when {
        config.screenWidthDp < 600 -> WindowType.Compact
        config.screenWidthDp < 840 -> WindowType.Medium
        else -> WindowType.Expanded
    }
}

@Composable
fun isLandscape(): Boolean {
    val config = LocalConfiguration.current
    return config.screenWidthDp > config.screenHeightDp
}
