package com.ssemaj.imdbapp.domain.usecase

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.ssemaj.imdbapp.TestFixtures
import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.data.paging.MoviePagingSource
import com.ssemaj.imdbapp.data.paging.MoviePagingSourceFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetNowPlayingMoviesUseCaseTest {

    private lateinit var useCase: GetNowPlayingMoviesUseCase
    private val pagingSourceFactory: MoviePagingSourceFactory = mock()
    private val pagingSource: MoviePagingSource = mock()

    @Before
    fun setup() {
        whenever(pagingSourceFactory.create()).thenReturn(pagingSource)
        useCase = GetNowPlayingMoviesUseCase(pagingSourceFactory)
    }

    @Test
    fun `invoke returns paging flow`() = runTest {
        val flow = useCase()

        assertThat(flow).isNotNull()
    }
}
