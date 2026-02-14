package com.ssemaj.imdbapp.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.data.paging.MoviePagingSourceFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNowPlayingMoviesUseCase @Inject constructor(
    private val pagingSourceFactory: MoviePagingSourceFactory
) {
    operator fun invoke(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 10
        ),
        pagingSourceFactory = pagingSourceFactory::create
    ).flow
}
