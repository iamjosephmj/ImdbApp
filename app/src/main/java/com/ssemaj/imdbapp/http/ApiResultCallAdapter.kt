package com.ssemaj.imdbapp.http

import com.ssemaj.imdbapp.data.api.ApiResult
import com.ssemaj.imdbapp.data.api.exception.ApiException
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * CallAdapter that wraps Retrofit responses in ApiResult.
 * Handles HTTP errors, network errors, and serialization errors.
 * 
 * Note: For suspend functions, Retrofit handles them specially and doesn't use CallAdapter.
 * This adapter works for non-suspend functions. For suspend functions, we'll use a different approach.
 */
internal class ApiResultCallAdapter<T>(
    private val responseType: java.lang.reflect.Type
) : CallAdapter<T, ApiResult<T>> {

    override fun responseType(): java.lang.reflect.Type = responseType

    override fun adapt(call: Call<T>): ApiResult<T> {
        return try {
            val response = call.execute()
            handleResponse(response)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun handleResponse(response: Response<T>): ApiResult<T> {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResult.Success(body)
            } else {
                ApiResult.Error(
                    ApiException.UnknownException(
                        message = "Response body is null",
                        cause = null
                    )
                )
            }
        } else {
            ApiResult.Error(HttpExceptionFactory.createHttpException(response.code(), response.message()))
        }
    }

    private fun handleException(throwable: Throwable): ApiResult<T> {
        return when (throwable) {
            is HttpException -> {
                ApiResult.Error(HttpExceptionFactory.createHttpException(throwable.code(), throwable.message()))
            }
            is SocketTimeoutException -> {
                ApiResult.Error(ApiException.NetworkException.TimeoutException(cause = throwable))
            }
            is UnknownHostException -> {
                ApiResult.Error(ApiException.NetworkException.NoConnectionException(cause = throwable))
            }
            is IOException -> {
                ApiResult.Error(
                    ApiException.NetworkException.GenericNetworkException(
                        message = "Network error: ${throwable.message ?: "Unknown error"}",
                        cause = throwable
                    )
                )
            }
            is kotlinx.serialization.SerializationException -> {
                ApiResult.Error(
                    ApiException.SerializationException(
                        message = "Failed to parse response: ${throwable.message}",
                        cause = throwable
                    )
                )
            }
            is ApiException -> {
                ApiResult.Error(throwable)
            }
            else -> {
                ApiResult.Error(
                    ApiException.UnknownException(
                        message = "Unexpected error: ${throwable.message ?: "Unknown error"}",
                        cause = throwable
                    )
                )
            }
        }
    }
}
