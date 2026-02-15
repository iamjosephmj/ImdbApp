package com.ssemaj.imdbapp.http

import com.ssemaj.imdbapp.data.api.ApiResult
import com.ssemaj.imdbapp.data.api.exception.ApiException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Extension function to wrap suspend function calls in ApiResult.
 * This handles errors from Retrofit suspend functions and converts them to ApiResult.
 */
internal suspend fun <T> suspendApiResultOf(block: suspend () -> T): ApiResult<T> {
    return try {
        ApiResult.Success(block())
    } catch (e: HttpException) {
        ApiResult.Error(HttpExceptionFactory.createHttpException(e.code(), e.message()))
    } catch (e: SocketTimeoutException) {
        ApiResult.Error(ApiException.NetworkException.TimeoutException(cause = e))
    } catch (e: UnknownHostException) {
        ApiResult.Error(ApiException.NetworkException.NoConnectionException(cause = e))
    } catch (e: IOException) {
        ApiResult.Error(
            ApiException.NetworkException.GenericNetworkException(
                message = "Network error: ${e.message ?: "Unknown error"}",
                cause = e
            )
        )
    } catch (e: kotlinx.serialization.SerializationException) {
        ApiResult.Error(
            ApiException.SerializationException(
                message = "Failed to parse response: ${e.message}",
                cause = e
            )
        )
    } catch (e: ApiException) {
        ApiResult.Error(e)
    } catch (e: Exception) {
        ApiResult.Error(
            ApiException.UnknownException(
                message = "Unexpected error: ${e.message ?: "Unknown error"}",
                cause = e
            )
        )
    }
}
