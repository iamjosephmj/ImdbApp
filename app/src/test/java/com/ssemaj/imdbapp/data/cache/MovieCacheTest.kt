package com.ssemaj.imdbapp.data.cache

import com.google.common.truth.Truth.assertThat
import com.ssemaj.imdbapp.TestFixtures
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class MovieCacheTest {

    private lateinit var cache: MovieCache

    @Before
    fun setup() {
        cache = MovieCache()
    }

    @Test
    fun `getDetails returns null for empty cache`() {
        assertThat(cache.getDetails(123)).isNull()
    }

    @Test
    fun `getDetails returns cached value`() {
        val details = TestFixtures.createMovieDetails(id = 123)
        cache.putDetails(123, details)

        assertThat(cache.getDetails(123)).isEqualTo(details)
    }

    @Test
    fun `putDetails overwrites existing`() {
        cache.putDetails(1, TestFixtures.createMovieDetails(id = 1, title = "Old"))
        cache.putDetails(1, TestFixtures.createMovieDetails(id = 1, title = "New"))

        assertThat(cache.getDetails(1)?.title).isEqualTo("New")
    }

    @Test
    fun `clearDetails removes all entries`() {
        cache.putDetails(1, TestFixtures.createMovieDetails(id = 1))
        cache.putDetails(2, TestFixtures.createMovieDetails(id = 2))

        cache.clearDetails()

        assertThat(cache.getDetails(1)).isNull()
        assertThat(cache.getDetails(2)).isNull()
    }

    @Test
    fun `cache handles multiple entries`() {
        cache.putDetails(1, TestFixtures.createMovieDetails(id = 1, title = "A"))
        cache.putDetails(2, TestFixtures.createMovieDetails(id = 2, title = "B"))

        assertThat(cache.getDetails(1)?.title).isEqualTo("A")
        assertThat(cache.getDetails(2)?.title).isEqualTo("B")
        assertThat(cache.getDetails(999)).isNull()
    }
}
