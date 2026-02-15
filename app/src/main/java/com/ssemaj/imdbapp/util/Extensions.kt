package com.ssemaj.imdbapp.util

import com.ssemaj.imdbapp.data.api.exception.ApiException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * String extensions for query validation and manipulation
 */
internal fun String.isValidQuery(minLength: Int = 2): Boolean = 
    trim().length >= minLength

internal fun String.toQueryOrEmpty(): String = trim().takeIf { it.isNotBlank() } ?: ""

internal fun String?.takeIfNotBlank(): String? = this?.takeIf { it.isNotBlank() }

internal fun String?.toUrl(baseUrl: String, size: String): String = 
    this?.let { "$baseUrl$size$it" } ?: ""

/**
 * Int extensions for time and page validation
 */
internal fun Int.toHoursAndMinutes(): Pair<Int, Int> = 
    Pair(this / 60, this % 60)

internal fun Int.isValidPage(): Boolean = this > 0

/**
 * Double extensions for rating formatting
 */
internal fun Double.formatRating(): String = 
    String.format(java.util.Locale.US, "%.1f", this)

internal fun Double.toPercentage(): String = 
    String.format(java.util.Locale.US, "%.0f%%", this * 10)

/**
 * List extensions for safe operations
 */
internal fun <T> List<T>.takeIfNotEmpty(): List<T>? = 
    if (isEmpty()) null else this

internal fun <T> List<T>.firstOrEmpty(): T? = 
    firstOrNull()

/**
 * Throwable extensions for error conversion
 */
internal fun Throwable.toApiException(): ApiException = when (this) {
    is ApiException -> this
    else -> ApiException.UnknownException(
        message = "Unexpected error: ${message ?: "Unknown error"}",
        cause = this
    )
}

internal fun Throwable.toUserMessage(defaultMessage: String = "An unexpected error occurred"): String = 
    message ?: defaultMessage

/**
 * String? extensions for null-safe operations
 */
internal fun String?.orEmpty(): String = this ?: ""

internal fun String?.takeIfNotNull(): String? = this
