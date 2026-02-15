package com.ssemaj.imdbapp.domain.usecase

import com.ssemaj.imdbapp.data.api.getOrThrow
import com.ssemaj.imdbapp.data.cache.MovieCache
import com.ssemaj.imdbapp.data.model.MovieDetails
import com.ssemaj.imdbapp.data.repository.MovieRepository
import javax.inject.Inject

internal class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val cache: MovieCache
) {
    suspend operator fun invoke(movieId: Int): MovieDetails = 
        cache.getDetails(movieId) ?: run {
            repository.fetchMovieDetails(movieId)
                .getOrThrow()
                .also { cache.putDetails(movieId, it) }
        }
}
