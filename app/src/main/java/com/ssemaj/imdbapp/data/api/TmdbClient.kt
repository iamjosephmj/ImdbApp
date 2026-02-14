package com.ssemaj.imdbapp.data.api

import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.data.model.MovieDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbClient @Inject constructor(private val api: TmdbService) {

    suspend fun nowPlaying(page: Int): PagedResult<Movie> {
        val response = api.getNowPlayingMovies(page, LANGUAGE)
        return PagedResult(response.results, response.page, response.totalPages)
    }

    suspend fun movieDetails(id: Int): MovieDetails = api.getMovieDetails(id, LANGUAGE)

    suspend fun search(query: String, page: Int = 1): List<Movie> =
        api.searchMovies(query, page, LANGUAGE, includeAdult = false).results

    companion object {
        private const val LANGUAGE = "en-US"
    }
}

data class PagedResult<T>(
    val items: List<T>,
    val page: Int,
    val totalPages: Int
) {
    val hasNextPage get() = page < totalPages
    val hasPrevPage get() = page > 1
}
