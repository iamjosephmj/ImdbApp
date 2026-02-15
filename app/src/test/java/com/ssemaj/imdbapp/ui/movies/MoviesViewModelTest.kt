package com.ssemaj.imdbapp.ui.movies

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.ssemaj.imdbapp.MainDispatcherRule
import com.ssemaj.imdbapp.domain.usecase.GetNowPlayingMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val useCase: GetNowPlayingMoviesUseCase = mock()

    private fun createViewModel(): MoviesViewModel {
        whenever(useCase()).thenReturn(flowOf(PagingData.empty()))
        return MoviesViewModel(useCase)
    }

    @Test
    fun `movies flow comes from use case`() = runTest {
        val viewModel = createViewModel()
        assertThat(viewModel.movies()).isNotNull()
        verify(useCase).invoke()
    }
}
