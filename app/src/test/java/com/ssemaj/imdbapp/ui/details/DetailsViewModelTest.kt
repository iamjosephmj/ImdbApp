package com.ssemaj.imdbapp.ui.details

import android.content.Context
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ssemaj.imdbapp.MainDispatcherRule
import com.ssemaj.imdbapp.R
import com.ssemaj.imdbapp.TestFixtures
import com.ssemaj.imdbapp.domain.ErrorMessageFormatter
import com.ssemaj.imdbapp.domain.ImageConfig
import com.ssemaj.imdbapp.domain.MovieFormatter
import com.ssemaj.imdbapp.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: DetailsViewModel
    private val useCase: GetMovieDetailsUseCase = mock()
    private val formatter: MovieFormatter = MovieFormatter()
    private val imageConfig: ImageConfig = ImageConfig()
    private val context: Context = mock()
    private val errorMessageFormatter: ErrorMessageFormatter = ErrorMessageFormatter(context)

    @Before
    fun setup() {
        viewModel = DetailsViewModel(useCase, formatter, imageConfig, errorMessageFormatter)
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
    fun `loadMovieDetails emits Error on ApiException`() = runTest {
        val exception = com.ssemaj.imdbapp.data.api.exception.ApiException.NetworkException.NoConnectionException()
        val errorMessage = "No internet connection. Please check your network settings."
        useCase.stub {
            onBlocking { invoke(123) } doAnswer { throw exception }
        }
        whenever(context.getString(R.string.error_no_connection)).thenReturn(errorMessage)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(DetailsUiState.Loading)

            viewModel.loadMovieDetails(123)

            val state = awaitItem()
            assertThat(state).isInstanceOf(DetailsUiState.Error::class.java)
            val errorState = state as DetailsUiState.Error
            assertThat(errorState.message).contains("No internet connection")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry reloads movie details`() = runTest {
        val details = TestFixtures.createMovieDetails(id = 123)
        val timeoutException = com.ssemaj.imdbapp.data.api.exception.ApiException.NetworkException.TimeoutException()
        var callCount = 0
        useCase.stub {
            onBlocking { invoke(123) } doAnswer {
                callCount++
                if (callCount == 1) {
                    throw timeoutException
                } else {
                    details
                }
            }
        }
        whenever(context.getString(R.string.error_timeout)).thenReturn("Request timed out. Please try again.")

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(DetailsUiState.Loading)

            viewModel.loadMovieDetails(123)
            val errorState = awaitItem() as DetailsUiState.Error
            assertThat(errorState.message).contains("timed out")

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
