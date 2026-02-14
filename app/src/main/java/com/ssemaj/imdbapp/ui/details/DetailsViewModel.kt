package com.ssemaj.imdbapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssemaj.imdbapp.domain.ImageConfig
import com.ssemaj.imdbapp.domain.MovieFormatter
import com.ssemaj.imdbapp.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val movieFormatter: MovieFormatter,
    private val imageConfig: ImageConfig
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "Failed to load"
    }

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            _uiState.value = try {
                val movie = getMovieDetailsUseCase(movieId)
                DetailsUiState.Success(
                    movie = movie,
                    formattedRating = movieFormatter.formatRating(movie.voteAverage),
                    formattedYear = movieFormatter.extractYear(movie.releaseDate),
                    formattedRuntime = movieFormatter.formatRuntime(movie.runtime),
                    formattedBudget = movieFormatter.formatCurrency(movie.budget),
                    formattedRevenue = movieFormatter.formatCurrency(movie.revenue),
                    formattedProductionCompanies = movieFormatter.formatProductionCompanies(movie.productionCompanies),
                    posterUrl = imageConfig.posterUrl(movie.posterPath),
                    backdropUrl = imageConfig.backdropUrl(movie.backdropPath)
                )
            } catch (e: Exception) {
                DetailsUiState.Error(e.message ?: DEFAULT_ERROR_MESSAGE)
            }
        }
    }

    fun retry(movieId: Int) = loadMovieDetails(movieId)
}
