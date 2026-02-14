package com.ssemaj.imdbapp.data.paging

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import com.ssemaj.imdbapp.TestFixtures
import com.ssemaj.imdbapp.data.api.PagedResult
import com.ssemaj.imdbapp.data.repository.MovieRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

class MoviePagingSourceTest {

    private lateinit var pagingSource: MoviePagingSource
    private val repository: MovieRepository = mock(stubOnly = true)

    @Before
    fun setup() {
        pagingSource = MoviePagingSource(repository)
    }

    @Test
    fun `load returns page with movies`() = runTest {
        val movies = TestFixtures.createMovieList(5)
        repository.stub {
            onBlocking { fetchNowPlayingMovies(1) } doReturn PagedResult(items = movies, page = 1, totalPages = 3)
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Page::class.java)
        val page = result as PagingSource.LoadResult.Page
        assertThat(page.data).hasSize(5)
    }

    @Test
    fun `load returns error on failure`() = runTest {
        repository.stub {
            onBlocking { fetchNowPlayingMovies(1) } doThrow RuntimeException("Network error")
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
    }

    @Test
    fun `prevKey is null on first page`() = runTest {
        repository.stub {
            onBlocking { fetchNowPlayingMovies(1) } doReturn PagedResult(
                items = emptyList(),
                page = 1,
                totalPages = 5
            )
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        ) as PagingSource.LoadResult.Page

        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `nextKey is null on last page`() = runTest {
        repository.stub {
            onBlocking { fetchNowPlayingMovies(5) } doReturn PagedResult(
                items = emptyList(),
                page = 5,
                totalPages = 5
            )
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Append(key = 5, loadSize = 20, placeholdersEnabled = false)
        ) as PagingSource.LoadResult.Page

        assertThat(result.nextKey).isNull()
    }

    @Test
    fun `nextKey increments when more pages exist`() = runTest {
        repository.stub {
            onBlocking { fetchNowPlayingMovies(2) } doReturn PagedResult(
                items = emptyList(),
                page = 2,
                totalPages = 5
            )
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Append(key = 2, loadSize = 20, placeholdersEnabled = false)
        ) as PagingSource.LoadResult.Page

        assertThat(result.nextKey).isEqualTo(3)
        assertThat(result.prevKey).isEqualTo(1)
    }
}
