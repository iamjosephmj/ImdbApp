package com.ssemaj.imdbapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ssemaj.imdbapp.data.api.ApiResult
import com.ssemaj.imdbapp.data.api.fold
import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.data.repository.MovieRepository

internal class MoviePagingSource(
    private val repository: MovieRepository
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val requestedPage = params.key ?: FIRST_PAGE
        
        return repository.fetchNowPlayingMovies(requestedPage).fold(
            onError = { exception -> LoadResult.Error(exception) },
            onSuccess = { pagedResult ->
                val (items, _, _) = pagedResult
                LoadResult.Page(
                    data = items,
                    prevKey = pagedResult.hasPrevPage.takeIf { it }?.let { requestedPage - 1 },
                    nextKey = pagedResult.hasNextPage.takeIf { it }?.let { requestedPage + 1 }
                )
            }
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = 
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { closestPage ->
                closestPage.prevKey?.plus(1) ?: closestPage.nextKey?.minus(1)
            }
        }

    companion object {
        private const val FIRST_PAGE = 1
    }
}

