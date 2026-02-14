package com.ssemaj.imdbapp.domain.usecase

import com.ssemaj.imdbapp.data.cache.MovieCache
import com.ssemaj.imdbapp.data.model.MovieDetails
import com.ssemaj.imdbapp.data.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val cache: MovieCache
) {
    suspend operator fun invoke(movieId: Int): MovieDetails {
        cache.getDetails(movieId)?.let { return it }

        val details = repository.fetchMovieDetails(movieId)
        cache.putDetails(movieId, details)
        return details
    }
}
