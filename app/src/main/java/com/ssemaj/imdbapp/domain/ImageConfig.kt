package com.ssemaj.imdbapp.domain

import javax.inject.Inject

class ImageConfig @Inject constructor() {
    private val baseUrl = "https://image.tmdb.org/t/p/"

    val posterSize = "w500"
    val backdropSize = "w780"

    fun posterUrl(path: String?): String {
        return path?.let { "$baseUrl$posterSize$it" } ?: ""
    }

    fun backdropUrl(path: String?): String {
        return path?.let { "$baseUrl$backdropSize$it" } ?: ""
    }
}
