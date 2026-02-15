package com.ssemaj.imdbapp.domain

import com.ssemaj.imdbapp.BuildConfig
import com.ssemaj.imdbapp.util.orEmpty
import javax.inject.Inject

internal class ImageConfig @Inject constructor() {
    private val baseUrl = BuildConfig.TMDB_IMAGE_BASE_URL

    val posterSize = BuildConfig.TMDB_POSTER_SIZE
    val backdropSize = BuildConfig.TMDB_BACKDROP_SIZE

    fun posterUrl(path: String?): String = 
        path?.let { buildString { append(baseUrl).append(posterSize).append(it) } }
            .orEmpty()

    fun backdropUrl(path: String?): String = 
        path?.let { buildString { append(baseUrl).append(backdropSize).append(it) } }
            .orEmpty()
}
