package com.ssemaj.imdbapp.data.repository

import com.google.common.truth.Truth.assertThat
import com.ssemaj.imdbapp.TestFixtures
import com.ssemaj.imdbapp.data.api.PagedResult
import com.ssemaj.imdbapp.data.api.TmdbClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class MovieRepositoryTest {

    private lateinit var repository: MovieRepository
    private val client: TmdbClient = mock()

    @Before
    fun setup() {
        repository = MovieRepository(client)
    }

    @Test
    fun `fetchNowPlayingMovies delegates to client`() = runTest {
        val movies = listOf(
            TestFixtures.createMovie(id = 1),
            TestFixtures.createMovie(id = 2)
        )
        val pagedResult = PagedResult(movies, page = 1, totalPages = 10)
        whenever(client.nowPlaying(1)).thenReturn(pagedResult)

        val result = repository.fetchNowPlayingMovies(1)

        assertThat(result).isEqualTo(pagedResult)
        verify(client).nowPlaying(1)
    }

    @Test
    fun `fetchMovieDetails delegates to client`() = runTest {
        val details = TestFixtures.createMovieDetails(id = 123)
        whenever(client.movieDetails(123)).thenReturn(details)

        val result = repository.fetchMovieDetails(123)

        assertThat(result).isEqualTo(details)
        verify(client).movieDetails(123)
    }

    @Test
    fun `fetchSearchResults delegates to client`() = runTest {
        val movies = listOf(
            TestFixtures.createMovie(id = 1, title = "Batman"),
            TestFixtures.createMovie(id = 2, title = "Batman Returns")
        )
        whenever(client.search("Batman", 1)).thenReturn(movies)

        val result = repository.fetchSearchResults("Batman")

        assertThat(result).hasSize(2)
        assertThat(result).isEqualTo(movies)
        verify(client).search("Batman", 1)
    }

    @Test
    fun `fetchSearchResults uses default page parameter`() = runTest {
        val movies = listOf(TestFixtures.createMovie(id = 1))
        whenever(client.search("query", 1)).thenReturn(movies)

        val result = repository.fetchSearchResults("query")

        assertThat(result).isEqualTo(movies)
        verify(client).search("query", 1)
    }
}
