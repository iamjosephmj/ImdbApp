package com.ssemaj.imdbapp.data.paging

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import com.ssemaj.imdbapp.TestFixtures
import com.ssemaj.imdbapp.data.api.ApiResult
import com.ssemaj.imdbapp.data.api.PagedResult
import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.data.repository.MovieRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
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
        val pagedResult = PagedResult(items = movies, page = 1, totalPages = 3)
        repository.stub {
            onBlocking { fetchNowPlayingMovies(1) } doReturn ApiResult.Success(pagedResult)
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Page::class.java)
        val page = result as PagingSource.LoadResult.Page
        assertThat(page.data).hasSize(5)
    }

    @Test
    fun `load returns error on ApiException`() = runTest {
        val exception = com.ssemaj.imdbapp.data.api.exception.ApiException.NetworkException.NoConnectionException()
        repository.stub {
            onBlocking { fetchNowPlayingMovies(1) } doReturn ApiResult.Error(exception)
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
        val errorResult = result as PagingSource.LoadResult.Error
        assertThat(errorResult.throwable).isEqualTo(exception)
    }

    @Test
    fun `prevKey is null on first page`() = runTest {
        val pagedResult = PagedResult<Movie>(
            items = emptyList(),
            page = 1,
            totalPages = 5
        )
        repository.stub {
            onBlocking { fetchNowPlayingMovies(1) } doReturn ApiResult.Success(pagedResult)
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        ) as PagingSource.LoadResult.Page

        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `nextKey is null on last page`() = runTest {
        val pagedResult = PagedResult<Movie>(
            items = emptyList(),
            page = 5,
            totalPages = 5
        )
        repository.stub {
            onBlocking { fetchNowPlayingMovies(5) } doReturn ApiResult.Success(pagedResult)
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Append(key = 5, loadSize = 20, placeholdersEnabled = false)
        ) as PagingSource.LoadResult.Page

        assertThat(result.nextKey).isNull()
    }

    @Test
    fun `nextKey increments when more pages exist`() = runTest {
        val pagedResult = PagedResult<Movie>(
            items = emptyList(),
            page = 2,
            totalPages = 5
        )
        repository.stub {
            onBlocking { fetchNowPlayingMovies(2) } doReturn ApiResult.Success(pagedResult)
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Append(key = 2, loadSize = 20, placeholdersEnabled = false)
        ) as PagingSource.LoadResult.Page

        assertThat(result.nextKey).isEqualTo(3)
        assertThat(result.prevKey).isEqualTo(1)
    }
}
