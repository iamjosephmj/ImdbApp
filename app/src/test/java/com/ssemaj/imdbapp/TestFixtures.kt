package com.ssemaj.imdbapp

import com.ssemaj.imdbapp.data.model.Genre
import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.data.model.MovieDetails
import com.ssemaj.imdbapp.data.model.MovieResponse
import com.ssemaj.imdbapp.data.model.ProductionCompany
import com.ssemaj.imdbapp.data.model.SpokenLanguage

object TestFixtures {

    fun createMovie(
        id: Int = 1,
        title: String = "Test Movie",
        overview: String = "Test overview",
        posterPath: String? = "/poster.jpg",
        backdropPath: String? = "/backdrop.jpg",
        voteAverage: Double = 7.5,
        voteCount: Int = 100,
        releaseDate: String? = "2024-01-15"
    ) = Movie(id, title, overview, posterPath, backdropPath, voteAverage, voteCount, releaseDate)

    fun createMovieDetails(
        id: Int = 1,
        title: String = "Test Movie",
        overview: String = "Test overview",
        posterPath: String? = "/poster.jpg",
        backdropPath: String? = "/backdrop.jpg",
        voteAverage: Double = 8.0,
        voteCount: Int = 200,
        releaseDate: String? = "2024-06-20",
        runtime: Int? = 120,
        status: String = "Released",
        tagline: String? = "Great movie",
        budget: Long = 100_000_000L,
        revenue: Long = 500_000_000L,
        genres: List<Genre> = listOf(Genre(1, "Action")),
        productionCompanies: List<ProductionCompany> = listOf(ProductionCompany(1, "Studio", "/logo.png", "US")),
        spokenLanguages: List<SpokenLanguage> = listOf(SpokenLanguage("English", "en", "English"))
    ) = MovieDetails(
        id, title, overview, posterPath, backdropPath, voteAverage, voteCount,
        releaseDate, runtime, status, tagline, budget, revenue, genres, productionCompanies, spokenLanguages
    )

    fun createMovieList(count: Int, withImages: Boolean = true) = (1..count).map { i ->
        createMovie(
            id = i,
            title = "Movie $i",
            posterPath = if (withImages) "/poster_$i.jpg" else null,
            backdropPath = if (withImages) "/backdrop_$i.jpg" else null,
            voteAverage = 5.0 + i * 0.5
        )
    }

    fun createMovieResponse(
        page: Int = 1,
        results: List<Movie> = listOf(createMovie()),
        totalPages: Int = 10,
        totalResults: Int = 200
    ) = MovieResponse(page, results, totalPages, totalResults)
}
