package com.ssemaj.imdbapp.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ImageConfigTest {

    private lateinit var imageConfig: ImageConfig

    @Before
    fun setup() {
        imageConfig = ImageConfig()
    }

    @Test
    fun `posterUrl builds correct URL`() {
        assertThat(imageConfig.posterUrl("/abc.jpg"))
            .isEqualTo("https://image.tmdb.org/t/p/w500/abc.jpg")
    }

    @Test
    fun `posterUrl returns empty for null`() {
        assertThat(imageConfig.posterUrl(null)).isEmpty()
    }

    @Test
    fun `backdropUrl builds correct URL`() {
        assertThat(imageConfig.backdropUrl("/bg.jpg"))
            .isEqualTo("https://image.tmdb.org/t/p/w780/bg.jpg")
    }

    @Test
    fun `backdropUrl returns empty for null`() {
        assertThat(imageConfig.backdropUrl(null)).isEmpty()
    }

    @Test
    fun `size properties are correct`() {
        assertThat(imageConfig.posterSize).isEqualTo("w500")
        assertThat(imageConfig.backdropSize).isEqualTo("w780")
    }
}
