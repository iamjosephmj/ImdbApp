package com.ssemaj.imdbapp.domain

import com.google.common.truth.Truth.assertThat
import com.ssemaj.imdbapp.data.model.Genre
import com.ssemaj.imdbapp.data.model.ProductionCompany
import org.junit.Before
import org.junit.Test

class MovieFormatterTest {

    private lateinit var formatter: MovieFormatter

    @Before
    fun setup() {
        formatter = MovieFormatter()
    }

    @Test
    fun `formatRating formats to one decimal`() {
        assertThat(formatter.formatRating(7.567)).isEqualTo("7.6")
        assertThat(formatter.formatRating(10.0)).isEqualTo("10.0")
        assertThat(formatter.formatRating(0.0)).isEqualTo("0.0")
        assertThat(formatter.formatRating(8.44)).isEqualTo("8.4")
    }

    @Test
    fun `extractYear returns year from date string`() {
        assertThat(formatter.extractYear("2024-06-15")).isEqualTo("2024")
        assertThat(formatter.extractYear("1999-12-31")).isEqualTo("1999")
    }

    @Test
    fun `extractYear handles null and short strings`() {
        assertThat(formatter.extractYear(null)).isEmpty()
        assertThat(formatter.extractYear("202")).isEqualTo("202")
    }

    @Test
    fun `formatRuntime formats hours and minutes`() {
        assertThat(formatter.formatRuntime(142)).isEqualTo("2h 22m")
        assertThat(formatter.formatRuntime(60)).isEqualTo("1h 0m")
        assertThat(formatter.formatRuntime(45)).isEqualTo("0h 45m")
    }

    @Test
    fun `formatRuntime returns empty for null`() {
        assertThat(formatter.formatRuntime(null)).isEmpty()
    }

    @Test
    fun `formatCurrency formats with dollar and commas`() {
        assertThat(formatter.formatCurrency(150_000_000L)).isEqualTo("$150,000,000")
        assertThat(formatter.formatCurrency(999L)).isEqualTo("$999")
    }

    @Test
    fun `formatCurrency returns NA for zero or negative`() {
        assertThat(formatter.formatCurrency(0L)).isEqualTo("N/A")
        assertThat(formatter.formatCurrency(-100L)).isEqualTo("N/A")
    }

    @Test
    fun `formatGenres joins names`() {
        val genres = listOf(
            Genre(1, "Action"),
            Genre(2, "Thriller"),
            Genre(3, "Drama")
        )
        assertThat(formatter.formatGenres(genres)).isEqualTo("Action, Thriller, Drama")
    }

    @Test
    fun `formatGenres handles single and empty`() {
        assertThat(formatter.formatGenres(listOf(Genre(1, "Comedy")))).isEqualTo("Comedy")
        assertThat(formatter.formatGenres(emptyList())).isEmpty()
    }

    @Test
    fun `formatProductionCompanies joins names`() {
        val companies = listOf(
            ProductionCompany(1, "Warner Bros", null, "US"),
            ProductionCompany(2, "Legendary", null, "US")
        )
        assertThat(formatter.formatProductionCompanies(companies)).isEqualTo("Warner Bros, Legendary")
    }

    @Test
    fun `formatProductionCompanies handles empty list`() {
        assertThat(formatter.formatProductionCompanies(emptyList())).isEmpty()
    }
}
