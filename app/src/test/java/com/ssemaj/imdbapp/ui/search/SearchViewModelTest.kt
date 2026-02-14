package com.ssemaj.imdbapp.ui.search

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ssemaj.imdbapp.MainDispatcherRule
import com.ssemaj.imdbapp.TestFixtures
import com.ssemaj.imdbapp.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SearchViewModel
    private val useCase: SearchMoviesUseCase = mock()

    @Before
    fun setup() {
        viewModel = SearchViewModel(useCase)
    }

    @Test
    fun `initial state is Idle`() = runTest {
        viewModel.searchResults.test {
            assertThat(awaitItem()).isEqualTo(SearchUiState.Idle)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onQueryChange updates query`() = runTest {
        viewModel.onQueryChange("test")
        assertThat(viewModel.searchQuery.value).isEqualTo("test")
    }

    @Test
    fun `clearSearch resets query`() = runTest {
        viewModel.onQueryChange("test")
        viewModel.clearSearch()
        assertThat(viewModel.searchQuery.value).isEmpty()
    }

    @Test
    fun `short query stays Idle`() = runTest {
        // Start collecting to activate the flow
        val job = launch { viewModel.searchResults.collect { } }

        viewModel.onQueryChange("a")
        advanceUntilIdle()

        assertThat(viewModel.searchResults.value).isEqualTo(SearchUiState.Idle)
        verify(useCase, never()).invoke(any())

        job.cancel()
    }

    @Test
    fun `search triggers after debounce`() = runTest {
        val movies = TestFixtures.createMovieList(2)
        whenever(useCase("test")).thenReturn(flowOf(movies))

        viewModel.searchResults.test {
            assertThat(awaitItem()).isEqualTo(SearchUiState.Idle)

            viewModel.onQueryChange("test")
            advanceTimeBy(350)

            // May get Loading then Success, or just Success
            val state = awaitItem()
            if (state is SearchUiState.Loading) {
                assertThat(awaitItem()).isInstanceOf(SearchUiState.Success::class.java)
            } else {
                assertThat(state).isInstanceOf(SearchUiState.Success::class.java)
            }
        }
    }

    @Test
    fun `debouncing prevents rapid searches`() = runTest {
        val movies = TestFixtures.createMovieList(1)
        whenever(useCase("final")).thenReturn(flowOf(movies))

        viewModel.searchResults.test {
            assertThat(awaitItem()).isEqualTo(SearchUiState.Idle)

            // Rapid typing - each resets the debounce timer
            viewModel.onQueryChange("fi")
            advanceTimeBy(100)
            viewModel.onQueryChange("fin")
            advanceTimeBy(100)
            viewModel.onQueryChange("fina")
            advanceTimeBy(100)
            viewModel.onQueryChange("final")
            advanceTimeBy(350) // Wait past debounce

            // Should only search for final query
            val state = awaitItem()
            if (state is SearchUiState.Loading) {
                awaitItem() // Success
            }

            verify(useCase).invoke("final")
        }
    }

    @Test
    fun `error state on failure`() = runTest {
        whenever(useCase("fail")).thenThrow(RuntimeException("Network error"))

        viewModel.searchResults.test {
            assertThat(awaitItem()).isEqualTo(SearchUiState.Idle)

            viewModel.onQueryChange("fail")
            advanceTimeBy(350)

            val state = awaitItem()
            if (state is SearchUiState.Loading) {
                assertThat(awaitItem()).isInstanceOf(SearchUiState.Error::class.java)
            } else {
                assertThat(state).isInstanceOf(SearchUiState.Error::class.java)
            }
        }
    }
}
