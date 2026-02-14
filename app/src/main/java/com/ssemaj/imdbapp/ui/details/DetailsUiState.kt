package com.ssemaj.imdbapp.ui.details

import com.ssemaj.imdbapp.data.model.MovieDetails

sealed interface DetailsUiState {
    data object Loading : DetailsUiState
    data class Success(
        val movie: MovieDetails,
        val formattedRating: String,
        val formattedYear: String,
        val formattedRuntime: String,
        val formattedBudget: String,
        val formattedRevenue: String,
        val formattedProductionCompanies: String,
        val posterUrl: String,
        val backdropUrl: String
    ) : DetailsUiState
    data class Error(val message: String) : DetailsUiState
}
