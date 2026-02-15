package com.ssemaj.imdbapp.data.cache

import android.util.LruCache
import com.ssemaj.imdbapp.data.model.MovieDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MovieCache @Inject constructor() {

    private val detailsCache = LruCache<Int, MovieDetails>(50)

    fun getDetails(id: Int): MovieDetails? = detailsCache.get(id)

    fun putDetails(id: Int, details: MovieDetails) {
        detailsCache.put(id, details)
    }

    fun clearDetails() = detailsCache.evictAll()
}
