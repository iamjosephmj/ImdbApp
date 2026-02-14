package com.ssemaj.imdbapp.data.paging

import com.ssemaj.imdbapp.data.repository.MovieRepository
import javax.inject.Inject

class MoviePagingSourceFactory @Inject constructor(
    private val repository: MovieRepository
) {
    fun create(): MoviePagingSource = MoviePagingSource(repository)
}
