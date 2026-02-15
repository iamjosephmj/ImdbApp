package com.ssemaj.imdbapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssemaj.imdbapp.data.api.exception.ApiException
import com.ssemaj.imdbapp.domain.ErrorMessageFormatter
import com.ssemaj.imdbapp.domain.ImageConfig
import com.ssemaj.imdbapp.domain.MovieFormatter
import com.ssemaj.imdbapp.domain.usecase.GetMovieDetailsUseCase
import com.ssemaj.imdbapp.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel @Inject internal constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val movieFormatter: MovieFormatter,
    private val imageConfig: ImageConfig,
    private val errorMessageFormatter: ErrorMessageFormatter
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadMovieDetails(movieId: Int) = viewModelScope.launch {
        _uiState.value = DetailsUiState.Loading
        _uiState.value = run {
            try {
                getMovieDetailsUseCase(movieId).let { movie ->
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
                }
            } catch (e: ApiException) {
                DetailsUiState.Error(errorMessageFormatter.format(e))
            } catch (e: Exception) {
                DetailsUiState.Error(e.toUserMessage("An unexpected error occurred"))
            }
        }
    }

    fun retry(movieId: Int) = loadMovieDetails(movieId)
}
