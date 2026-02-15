package com.ssemaj.imdbapp.domain

import com.ssemaj.imdbapp.data.model.Genre
import com.ssemaj.imdbapp.data.model.ProductionCompany
import com.ssemaj.imdbapp.util.orEmpty
import com.ssemaj.imdbapp.util.takeIfNotEmpty
import com.ssemaj.imdbapp.util.toHoursAndMinutes
import java.util.Locale
import javax.inject.Inject

internal class MovieFormatter @Inject constructor() {

    fun formatRating(voteAverage: Double): String = 
        String.format(Locale.US, "%.1f", voteAverage)

    fun extractYear(releaseDate: String?): String = 
        releaseDate?.take(4).orEmpty()

    fun formatRuntime(runtime: Int?): String = 
        runtime?.let { 
            val (hours, minutes) = it.toHoursAndMinutes()
            "${hours}h ${minutes}m"
        }.orEmpty()

    fun formatCurrency(amount: Long): String = 
        amount.takeIf { it > 0 }
            ?.let { "$${String.format(Locale.US, "%,d", it)}" }
            ?: "N/A"

    fun formatGenres(genres: List<Genre>): String = 
        genres.map { it.name }
            .takeIfNotEmpty()
            ?.joinToString(", ")
            .orEmpty()

    fun formatProductionCompanies(companies: List<ProductionCompany>): String = 
        companies.map { it.name }
            .takeIfNotEmpty()
            ?.joinToString(", ")
            .orEmpty()
}
