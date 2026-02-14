package com.ssemaj.imdbapp.di

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * CompositionLocal for providing ViewModelFactory to Compose.
 */
val LocalViewModelFactory = staticCompositionLocalOf<ViewModelFactory> {
    error("ViewModelFactory not provided")
}
