package com.ssemaj.imdbapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int,
    @SerialName("release_date") val releaseDate: String? = null,
    val runtime: Int? = null,
    val status: String,
    val tagline: String? = null,
    val budget: Long,
    val revenue: Long,
    val genres: List<Genre>,
    @SerialName("production_companies") val productionCompanies: List<ProductionCompany>,
    @SerialName("spoken_languages") val spokenLanguages: List<SpokenLanguage>
)
