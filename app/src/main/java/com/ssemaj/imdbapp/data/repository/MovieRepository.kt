package com.ssemaj.imdbapp.data.repository

import com.ssemaj.imdbapp.data.api.PagedResult
import com.ssemaj.imdbapp.data.api.TmdbClient
import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.data.model.MovieDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val client: TmdbClient
) {
    suspend fun fetchNowPlayingMovies(page: Int): PagedResult<Movie> = client.nowPlaying(page)

    suspend fun fetchMovieDetails(movieId: Int): MovieDetails = client.movieDetails(movieId)

    suspend fun fetchSearchResults(query: String, page: Int = 1): List<Movie> = client.search(query, page)
}
