package com.ssemaj.imdbapp.domain.usecase

import com.ssemaj.imdbapp.data.api.ApiResult
import com.ssemaj.imdbapp.data.cache.MovieCache
import com.ssemaj.imdbapp.data.model.MovieDetails
import com.ssemaj.imdbapp.data.repository.MovieRepository
import javax.inject.Inject

internal class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val cache: MovieCache
) {
    suspend operator fun invoke(movieId: Int): MovieDetails {
        cache.getDetails(movieId)?.let { return it }

        return when (val result = repository.fetchMovieDetails(movieId)) {
            is ApiResult.Success -> {
                val details = result.data
                cache.putDetails(movieId, details)
                details
            }
            is ApiResult.Error -> throw result.exception
        }
    }
}
