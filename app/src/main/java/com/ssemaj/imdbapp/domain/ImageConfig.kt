package com.ssemaj.imdbapp.domain

import com.ssemaj.imdbapp.BuildConfig
import javax.inject.Inject

internal class ImageConfig @Inject constructor() {
    private val baseUrl = BuildConfig.TMDB_IMAGE_BASE_URL

    val posterSize = BuildConfig.TMDB_POSTER_SIZE
    val backdropSize = BuildConfig.TMDB_BACKDROP_SIZE

    fun posterUrl(path: String?): String {
        return path?.let { "$baseUrl$posterSize$it" } ?: ""
    }

    fun backdropUrl(path: String?): String {
        return path?.let { "$baseUrl$backdropSize$it" } ?: ""
    }
}
