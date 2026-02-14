package com.ssemaj.imdbapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.data.repository.MovieRepository

class MoviePagingSource(
    private val repository: MovieRepository
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val requestedPage = params.key ?: FIRST_PAGE
        
        return try {
            val pagedResult = repository.fetchNowPlayingMovies(requestedPage)
            
            LoadResult.Page(
                data = pagedResult.items,
                prevKey = if (pagedResult.hasPrevPage) requestedPage - 1 else null,
                nextKey = if (pagedResult.hasNextPage) requestedPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        // Return the page key that would load the page containing the anchor position
        // This ensures smooth scrolling when the list is refreshed
        val anchorPosition = state.anchorPosition ?: return null
        val closestPage = state.closestPageToPosition(anchorPosition) ?: return null
        
        // Prefer the page after the previous page, or the page before the next page
        return closestPage.prevKey?.plus(1) ?: closestPage.nextKey?.minus(1)
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}
