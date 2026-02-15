package com.ssemaj.imdbapp.data.api

import com.ssemaj.imdbapp.data.api.exception.ApiException

/**
 * Sealed class representing the result of an API call.
 * Provides type-safe error handling with explicit success and error states.
 *
 * @param T The type of data returned on success
 */
sealed class ApiResult<out T> {
    /**
     * Represents a successful API call with data
     */
    data class Success<T>(val data: T) : ApiResult<T>()

    /**
     * Represents a failed API call with an error
     */
    data class Error<T>(val exception: ApiException) : ApiResult<T>()
}
