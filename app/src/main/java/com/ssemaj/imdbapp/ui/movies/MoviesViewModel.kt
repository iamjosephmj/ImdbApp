package com.ssemaj.imdbapp.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ssemaj.imdbapp.domain.usecase.GetNowPlayingMoviesUseCase
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase
) : ViewModel() {

    val movies = getNowPlayingMoviesUseCase().cachedIn(viewModelScope)
}
