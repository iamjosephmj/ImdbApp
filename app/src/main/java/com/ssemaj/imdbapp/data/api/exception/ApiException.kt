package com.ssemaj.imdbapp.data.api.exception

import okio.IOException

/**
 * Base sealed class for all API-related exceptions.
 * Provides a hierarchy for different types of errors that can occur during API calls.
 * Extends IOException to be compatible with OkHttp interceptors.
 */
sealed class ApiException(
    message: String,
    cause: Throwable? = null
) : IOException(message, cause) {

    /**
     * Network-related exceptions (connectivity issues, timeouts, etc.)
     */
    sealed class NetworkException(
        message: String,
        cause: Throwable? = null
    ) : ApiException(message, cause) {
        
        /**
         * No internet connection available
         */
        class NoConnectionException(
            cause: Throwable? = null
        ) : NetworkException(
            message = "No internet connection available",
            cause = cause
        )

        /**
         * Request timeout
         */
        class TimeoutException(
            cause: Throwable? = null
        ) : NetworkException(
            message = "Request timed out",
            cause = cause
        )

        /**
         * Generic network error
         */
        class GenericNetworkException(
            message: String = "Network error occurred",
            cause: Throwable? = null
        ) : NetworkException(message, cause)
    }

    /**
     * HTTP-related exceptions (4xx, 5xx status codes)
     */
    sealed class HttpException(
        val statusCode: Int,
        val statusMessage: String? = null,
        message: String? = null,
        cause: Throwable? = null
    ) : ApiException(
        message ?: "HTTP error: $statusCode ${statusMessage ?: ""}",
        cause
    ) {

        /**
         * Client errors (4xx status codes)
         */
        sealed class ClientException(
            statusCode: Int,
            statusMessage: String? = null,
            message: String? = null,
            cause: Throwable? = null
        ) : HttpException(statusCode, statusMessage, message, cause) {

            /**
             * Bad Request (400)
             */
            class BadRequestException(
                statusMessage: String? = null,
                cause: Throwable? = null
            ) : ClientException(
                statusCode = 400,
                statusMessage = statusMessage,
                message = "Bad request",
                cause = cause
            )

            /**
             * Unauthorized (401)
             */
            class UnauthorizedException(
                statusMessage: String? = null,
                cause: Throwable? = null
            ) : ClientException(
                statusCode = 401,
                statusMessage = statusMessage,
                message = "Unauthorized - Invalid API key",
                cause = cause
            )

            /**
             * Forbidden (403)
             */
            class ForbiddenException(
                statusMessage: String? = null,
                cause: Throwable? = null
            ) : ClientException(
                statusCode = 403,
                statusMessage = statusMessage,
                message = "Forbidden - Access denied",
                cause = cause
            )

            /**
             * Not Found (404)
             */
            class NotFoundException(
                statusMessage: String? = null,
                cause: Throwable? = null
            ) : ClientException(
                statusCode = 404,
                statusMessage = statusMessage,
                message = "Resource not found",
                cause = cause
            )

            /**
             * Too Many Requests (429)
             */
            class TooManyRequestsException(
                statusMessage: String? = null,
                cause: Throwable? = null
            ) : ClientException(
                statusCode = 429,
                statusMessage = statusMessage,
                message = "Too many requests - Rate limit exceeded",
                cause = cause
            )

            /**
             * Generic client error (other 4xx)
             */
            class GenericClientException(
                statusCode: Int,
                statusMessage: String? = null,
                message: String? = null,
                cause: Throwable? = null
            ) : ClientException(statusCode, statusMessage, message, cause)
        }

        /**
         * Server errors (5xx status codes)
         */
        sealed class ServerException(
            statusCode: Int,
            statusMessage: String? = null,
            message: String? = null,
            cause: Throwable? = null
        ) : HttpException(statusCode, statusMessage, message, cause) {

            /**
             * Internal Server Error (500)
             */
            class InternalServerErrorException(
                statusMessage: String? = null,
                cause: Throwable? = null
            ) : ServerException(
                statusCode = 500,
                statusMessage = statusMessage,
                message = "Internal server error",
                cause = cause
            )

            /**
             * Bad Gateway (502)
             */
            class BadGatewayException(
                statusMessage: String? = null,
                cause: Throwable? = null
            ) : ServerException(
                statusCode = 502,
                statusMessage = statusMessage,
                message = "Bad gateway",
                cause = cause
            )

            /**
             * Service Unavailable (503)
             */
            class ServiceUnavailableException(
                statusMessage: String? = null,
                cause: Throwable? = null
            ) : ServerException(
                statusCode = 503,
                statusMessage = statusMessage,
                message = "Service temporarily unavailable",
                cause = cause
            )

            /**
             * Gateway Timeout (504)
             */
            class GatewayTimeoutException(
                statusMessage: String? = null,
                cause: Throwable? = null
            ) : ServerException(
                statusCode = 504,
                statusMessage = statusMessage,
                message = "Gateway timeout",
                cause = cause
            )

            /**
             * Generic server error (other 5xx)
             */
            class GenericServerException(
                statusCode: Int,
                statusMessage: String? = null,
                message: String? = null,
                cause: Throwable? = null
            ) : ServerException(statusCode, statusMessage, message, cause)
        }
    }

    /**
     * JSON serialization/deserialization errors
     */
    class SerializationException(
        message: String = "Failed to parse response",
        cause: Throwable? = null
    ) : ApiException(message, cause)

    /**
     * Unknown or unexpected errors
     */
    class UnknownException(
        message: String = "An unexpected error occurred",
        cause: Throwable? = null
    ) : ApiException(message, cause)
}
