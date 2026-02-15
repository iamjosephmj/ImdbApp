package com.ssemaj.imdbapp.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.ssemaj.imdbapp.TestFixtures
import com.ssemaj.imdbapp.data.api.ApiResult
import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.data.repository.MovieRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SearchMoviesUseCaseTest {

    private lateinit var useCase: SearchMoviesUseCase
    private val repository: MovieRepository = mock()

    @Before
    fun setup() {
        useCase = SearchMoviesUseCase(repository)
    }

    @Test
    fun `invoke returns results from repository`() = runTest {
        val movies = listOf(
            TestFixtures.createMovie(id = 1, title = "Batman"),
            TestFixtures.createMovie(id = 2, title = "Batman Returns")
        )
        whenever(repository.fetchSearchResults("Batman")).thenReturn(ApiResult.Success(movies))

        val result = useCase("Batman").first()

        assertThat(result).hasSize(2)
        assertThat(result).isEqualTo(movies)
        verify(repository).fetchSearchResults("Batman")
    }

    @Test
    fun `invoke returns empty list for blank query`() = runTest {
        val result = useCase("   ").first()

        assertThat(result).isEmpty()
        verify(repository, never()).fetchSearchResults(any(), any())
    }

    @Test
    fun `invoke returns empty list for empty query`() = runTest {
        val result = useCase("").first()

        assertThat(result).isEmpty()
        verify(repository, never()).fetchSearchResults(any(), any())
    }
}
