package com.ssemaj.imdbapp.data.api

import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.data.model.MovieDetails
import com.ssemaj.imdbapp.http.suspendApiResultOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TmdbClient @Inject constructor(private val api: TmdbService) {

    suspend fun nowPlaying(page: Int): ApiResult<PagedResult<Movie>> =
        suspendApiResultOf {
            api.getNowPlayingMovies(page, LANGUAGE).let { response ->
                PagedResult(response.results, response.page, response.totalPages)
            }
        }

    suspend fun movieDetails(id: Int): ApiResult<MovieDetails> =
        suspendApiResultOf { api.getMovieDetails(id, LANGUAGE) }

    suspend fun search(query: String, page: Int = 1): ApiResult<List<Movie>> =
        suspendApiResultOf {
            api.searchMovies(query, page, LANGUAGE, includeAdult = false).results
        }

    companion object {
        private const val LANGUAGE = "en-US"
    }
}
