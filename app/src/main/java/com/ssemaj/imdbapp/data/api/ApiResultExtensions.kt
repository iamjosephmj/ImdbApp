package com.ssemaj.imdbapp.data.api

import com.ssemaj.imdbapp.data.api.exception.ApiException

/**
 * Extension properties for ApiResult
 */
internal val <T> ApiResult.Success<T>.value: T
    get() = data

internal val <T> ApiResult.Error<T>.error: ApiException
    get() = exception

internal fun <T> ApiResult<T>.getOrThrow(): T = when (this) {
    is ApiResult.Success -> data
    is ApiResult.Error -> throw exception
}

internal fun <T, R> ApiResult<T>.fold(
    onError: (ApiException) -> R,
    onSuccess: (T) -> R
): R = when (this) {
    is ApiResult.Success -> onSuccess(data)
    is ApiResult.Error -> onError(exception)
}
