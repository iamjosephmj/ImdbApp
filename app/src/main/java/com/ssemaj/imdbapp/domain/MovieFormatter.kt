package com.ssemaj.imdbapp.domain

import com.ssemaj.imdbapp.data.model.Genre
import com.ssemaj.imdbapp.data.model.ProductionCompany
import java.util.Locale
import javax.inject.Inject

internal class MovieFormatter @Inject constructor() {

    fun formatRating(voteAverage: Double): String {
        return String.format(Locale.US, "%.1f", voteAverage)
    }

    fun extractYear(releaseDate: String?): String {
        return releaseDate?.take(4) ?: ""
    }

    fun formatRuntime(runtime: Int?): String {
        return runtime?.let { "${it / 60}h ${it % 60}m" } ?: ""
    }

    fun formatCurrency(amount: Long): String {
        return if (amount > 0) {
            "$${String.format(Locale.US, "%,d", amount)}"
        } else {
            "N/A"
        }
    }

    fun formatGenres(genres: List<Genre>): String {
        return genres.joinToString(", ") { it.name }
    }

    fun formatProductionCompanies(companies: List<ProductionCompany>): String {
        return companies.joinToString(", ") { it.name }
    }
}
