package com.ssemaj.imdbapp.http

import com.ssemaj.imdbapp.data.api.exception.ApiException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Interceptor that handles network-level errors (connection issues, timeouts).
 * HTTP errors (4xx, 5xx) are handled by Retrofit and converted in suspendApiResultOf.
 */
internal class ErrorInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: ApiException) {
            // Re-throw ApiException as-is
            throw e
        } catch (e: SocketTimeoutException) {
            throw ApiException.NetworkException.TimeoutException(cause = e)
        } catch (e: UnknownHostException) {
            throw ApiException.NetworkException.NoConnectionException(cause = e)
        } catch (e: IOException) {
            throw ApiException.NetworkException.GenericNetworkException(
                message = "Network error: ${e.message ?: "Unknown error"}",
                cause = e
            )
        } catch (e: Exception) {
            throw ApiException.UnknownException(
                message = "Unexpected error: ${e.message ?: "Unknown error"}",
                cause = e
            )
        }
    }
}
