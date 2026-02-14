package com.ssemaj.imdbapp.data.api

import com.google.common.truth.Truth.assertThat
import com.ssemaj.imdbapp.TestFixtures
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TmdbClientTest {

    private lateinit var client: TmdbClient
    private val api: TmdbService = mock()

    @Before
    fun setup() {
        client = TmdbClient(api)
    }

    @Test
    fun `nowPlaying returns paged result`() = runTest {
        val movies = TestFixtures.createMovieList(3)
        val response = TestFixtures.createMovieResponse(page = 1, results = movies, totalPages = 5)
        whenever(api.getNowPlayingMovies(1, "en-US")).thenReturn(response)

        val result = client.nowPlaying(1)

        assertThat(result.items).hasSize(3)
        assertThat(result.page).isEqualTo(1)
        assertThat(result.hasNextPage).isTrue()
        assertThat(result.hasPrevPage).isFalse()
    }

    @Test
    fun `movieDetails passes language param`() = runTest {
        val details = TestFixtures.createMovieDetails(id = 42)
        whenever(api.getMovieDetails(42, "en-US")).thenReturn(details)

        val result = client.movieDetails(42)

        assertThat(result.id).isEqualTo(42)
        verify(api).getMovieDetails(42, "en-US")
    }

    @Test
    fun `search uses correct defaults`() = runTest {
        val movies = TestFixtures.createMovieList(2)
        val response = TestFixtures.createMovieResponse(results = movies)
        whenever(api.searchMovies("batman", 1, "en-US", false)).thenReturn(response)

        val result = client.search("batman")

        assertThat(result).hasSize(2)
        verify(api).searchMovies("batman", 1, "en-US", false)
    }
}
