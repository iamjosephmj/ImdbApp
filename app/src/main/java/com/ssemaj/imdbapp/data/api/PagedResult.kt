package com.ssemaj.imdbapp.data.api

data class PagedResult<T>(
    val items: List<T>,
    val page: Int,
    val totalPages: Int
) {
    val hasNextPage get() = page < totalPages
    val hasPrevPage get() = page > 1

    operator fun compareTo(other: PagedResult<*>): Int = page.compareTo(other.page)

    operator fun rangeTo(other: PagedResult<*>): IntRange = page..other.page
}
