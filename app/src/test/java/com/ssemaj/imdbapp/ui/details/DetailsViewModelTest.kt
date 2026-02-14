package com.ssemaj.imdbapp.ui.details

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ssemaj.imdbapp.MainDispatcherRule
import com.ssemaj.imdbapp.TestFixtures
import com.ssemaj.imdbapp.domain.ImageConfig
import com.ssemaj.imdbapp.domain.MovieFormatter
import com.ssemaj.imdbapp.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class DetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: DetailsViewModel
    private val useCase: GetMovieDetailsUseCase = mock()
    private val formatter: MovieFormatter = MovieFormatter()
    private val imageConfig: ImageConfig = ImageConfig()

    @Before
    fun setup() {
        viewModel = DetailsViewModel(useCase, formatter, imageConfig)
    }

    @Test
    fun `loadMovieDetails emits Success with formatted data`() = runTest {
        val details = TestFixtures.createMovieDetails(id = 123)
        useCase.stub {
            onBlocking { invoke(123) } doReturn details
        }

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(DetailsUiState.Loading)

            viewModel.loadMovieDetails(123)

            val state = awaitItem()
            if (state is DetailsUiState.Loading) {
                val successState = awaitItem() as DetailsUiState.Success
                assertThat(successState.movie).isEqualTo(details)
                assertThat(successState.formattedRating).isEqualTo(formatter.formatRating(details.voteAverage))
                assertThat(successState.formattedYear).isEqualTo(formatter.extractYear(details.releaseDate))
                assertThat(successState.formattedRuntime).isEqualTo(formatter.formatRuntime(details.runtime))
                assertThat(successState.posterUrl).isEqualTo(imageConfig.posterUrl(details.posterPath))
                assertThat(successState.backdropUrl).isEqualTo(imageConfig.backdropUrl(details.backdropPath))
            } else {
                val successState = state as DetailsUiState.Success
                assertThat(successState.movie).isEqualTo(details)
                assertThat(successState.formattedRating).isEqualTo(formatter.formatRating(details.voteAverage))
                assertThat(successState.formattedYear).isEqualTo(formatter.extractYear(details.releaseDate))
                assertThat(successState.formattedRuntime).isEqualTo(formatter.formatRuntime(details.runtime))
                assertThat(successState.posterUrl).isEqualTo(imageConfig.posterUrl(details.posterPath))
                assertThat(successState.backdropUrl).isEqualTo(imageConfig.backdropUrl(details.backdropPath))
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadMovieDetails emits Error on failure`() = runTest {
        useCase.stub {
            onBlocking { invoke(123) } doThrow RuntimeException("Network error")
        }

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(DetailsUiState.Loading)

            viewModel.loadMovieDetails(123)

            val state = awaitItem()
            assertThat(state).isInstanceOf(DetailsUiState.Error::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry reloads movie details`() = runTest {
        val details = TestFixtures.createMovieDetails(id = 123)
        var callCount = 0
        useCase.stub {
            onBlocking { invoke(123) } doAnswer {
                callCount++
                if (callCount == 1) {
                    throw RuntimeException("First fail")
                } else {
                    details
                }
            }
        }

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(DetailsUiState.Loading)

            viewModel.loadMovieDetails(123)
            assertThat(awaitItem()).isInstanceOf(DetailsUiState.Error::class.java)

            viewModel.retry(123)
            val state = awaitItem()
            if (state is DetailsUiState.Loading) {
                val successState = awaitItem() as DetailsUiState.Success
                assertThat(successState.movie).isEqualTo(details)
            } else {
                val successState = state as DetailsUiState.Success
                assertThat(successState.movie).isEqualTo(details)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }
}
