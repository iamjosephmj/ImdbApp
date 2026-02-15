package com.ssemaj.imdbapp.data.repository

import com.ssemaj.imdbapp.data.api.ApiResult
import com.ssemaj.imdbapp.data.api.PagedResult
import com.ssemaj.imdbapp.data.api.TmdbClient
import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.data.model.MovieDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MovieRepository @Inject constructor(
    private val client: TmdbClient
) {
    suspend fun fetchNowPlayingMovies(page: Int): ApiResult<PagedResult<Movie>> {
        return client.nowPlaying(page)
    }

    suspend fun fetchMovieDetails(movieId: Int): ApiResult<MovieDetails> {
        return client.movieDetails(movieId)
    }

    suspend fun fetchSearchResults(query: String, page: Int = 1): ApiResult<List<Movie>> {
        return client.search(query, page)
    }
}
