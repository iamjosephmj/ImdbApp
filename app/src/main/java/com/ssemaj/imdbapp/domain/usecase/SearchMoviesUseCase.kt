package com.ssemaj.imdbapp.domain.usecase

import com.ssemaj.imdbapp.data.api.ApiResult
import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class SearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(query: String): Flow<List<Movie>> = flow {
        if (query.isBlank()) {
            emit(emptyList())
        } else {
            when (val result = repository.fetchSearchResults(query)) {
                is ApiResult.Success -> emit(result.data)
                is ApiResult.Error -> throw result.exception
            }
        }
    }
}
