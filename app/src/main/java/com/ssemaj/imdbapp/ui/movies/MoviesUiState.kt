package com.ssemaj.imdbapp.ui.movies

sealed interface MoviesUiState {
    data object Loading : MoviesUiState
    data object Success : MoviesUiState
    data class Error(val message: String) : MoviesUiState
}
