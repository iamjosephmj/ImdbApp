package com.ssemaj.imdbapp.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.domain.usecase.GetNowPlayingMoviesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesViewModel @Inject internal constructor(
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase
) : ViewModel() {

    private val _movies: Flow<PagingData<Movie>> by lazy {
        getNowPlayingMoviesUseCase().cachedIn(viewModelScope)
    }

    fun movies(): Flow<PagingData<Movie>> = _movies
}
