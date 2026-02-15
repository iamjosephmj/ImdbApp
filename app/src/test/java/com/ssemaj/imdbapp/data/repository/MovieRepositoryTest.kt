package com.ssemaj.imdbapp.data.repository

import com.google.common.truth.Truth.assertThat
import com.ssemaj.imdbapp.TestFixtures
import com.ssemaj.imdbapp.data.api.ApiResult
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
    fun `fetchNowPlayingMovies returns success result`() = runTest {
        val movies = listOf(
            TestFixtures.createMovie(id = 1),
            TestFixtures.createMovie(id = 2)
        )
        val pagedResult = PagedResult(movies, page = 1, totalPages = 10)
        whenever(client.nowPlaying(1)).thenReturn(ApiResult.Success(pagedResult))

        val result = repository.fetchNowPlayingMovies(1)

        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        assertThat((result as ApiResult.Success).data).isEqualTo(pagedResult)
        verify(client).nowPlaying(1)
    }

    @Test
    fun `fetchNowPlayingMovies returns error result`() = runTest {
        val exception = com.ssemaj.imdbapp.data.api.exception.ApiException.NetworkException.NoConnectionException()
        whenever(client.nowPlaying(1)).thenReturn(ApiResult.Error(exception))

        val result = repository.fetchNowPlayingMovies(1)

        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        assertThat((result as ApiResult.Error).exception).isEqualTo(exception)
    }

    @Test
    fun `fetchMovieDetails returns success result`() = runTest {
        val details = TestFixtures.createMovieDetails(id = 123)
        whenever(client.movieDetails(123)).thenReturn(ApiResult.Success(details))

        val result = repository.fetchMovieDetails(123)

        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        assertThat((result as ApiResult.Success).data).isEqualTo(details)
        verify(client).movieDetails(123)
    }

    @Test
    fun `fetchMovieDetails returns error result`() = runTest {
        val exception = com.ssemaj.imdbapp.data.api.exception.ApiException.HttpException.ClientException.NotFoundException()
        whenever(client.movieDetails(123)).thenReturn(ApiResult.Error(exception))

        val result = repository.fetchMovieDetails(123)

        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        assertThat((result as ApiResult.Error).exception).isEqualTo(exception)
    }

    @Test
    fun `fetchSearchResults returns success result`() = runTest {
        val movies = listOf(
            TestFixtures.createMovie(id = 1, title = "Batman"),
            TestFixtures.createMovie(id = 2, title = "Batman Returns")
        )
        whenever(client.search("Batman", 1)).thenReturn(ApiResult.Success(movies))

        val result = repository.fetchSearchResults("Batman")

        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        assertThat((result as ApiResult.Success).data).hasSize(2)
        assertThat((result as ApiResult.Success).data).isEqualTo(movies)
        verify(client).search("Batman", 1)
    }

    @Test
    fun `fetchSearchResults uses default page parameter`() = runTest {
        val movies = listOf(TestFixtures.createMovie(id = 1))
        whenever(client.search("query", 1)).thenReturn(ApiResult.Success(movies))

        val result = repository.fetchSearchResults("query")

        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        assertThat((result as ApiResult.Success).data).isEqualTo(movies)
        verify(client).search("query", 1)
    }
}
