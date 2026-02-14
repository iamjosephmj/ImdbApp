package com.ssemaj.imdbapp.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.ssemaj.imdbapp.TestFixtures
import com.ssemaj.imdbapp.data.cache.MovieCache
import com.ssemaj.imdbapp.data.repository.MovieRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetMovieDetailsUseCaseTest {

    private lateinit var useCase: GetMovieDetailsUseCase
    private val repository: MovieRepository = mock()
    private val cache: MovieCache = mock()

    @Before
    fun setup() {
        useCase = GetMovieDetailsUseCase(repository, cache)
    }

    @Test
    fun `invoke returns cached value when available`() = runTest {
        val cached = TestFixtures.createMovieDetails(id = 123)
        whenever(cache.getDetails(123)).thenReturn(cached)

        val result = useCase(123)

        assertThat(result).isEqualTo(cached)
        verify(repository, never()).fetchMovieDetails(any())
    }

    @Test
    fun `invoke fetches from repository when not cached`() = runTest {
        val details = TestFixtures.createMovieDetails(id = 123)
        whenever(cache.getDetails(123)).thenReturn(null)
        whenever(repository.fetchMovieDetails(123)).thenReturn(details)

        val result = useCase(123)

        assertThat(result).isEqualTo(details)
        verify(repository).fetchMovieDetails(123)
        verify(cache).putDetails(123, details)
    }

    @Test
    fun `invoke caches result after fetching`() = runTest {
        val details = TestFixtures.createMovieDetails(id = 456)
        whenever(cache.getDetails(456)).thenReturn(null)
        whenever(repository.fetchMovieDetails(456)).thenReturn(details)

        useCase(456)

        verify(cache).putDetails(456, details)
    }
}
