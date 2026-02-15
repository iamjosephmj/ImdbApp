package com.ssemaj.imdbapp.http

import com.ssemaj.imdbapp.data.api.exception.ApiException
import java.net.HttpURLConnection

/**
 * Factory for creating ApiException instances based on HTTP status codes.
 * Centralizes HTTP error handling logic to avoid code duplication.
 */
internal object HttpExceptionFactory {

    /**
     * Creates an appropriate ApiException based on HTTP status code.
     *
     * @param statusCode The HTTP status code
     * @param statusMessage Optional status message from the response
     * @return An ApiException instance corresponding to the status code
     */
    fun createHttpException(statusCode: Int, statusMessage: String?): ApiException {
        return when (statusCode) {
            // Client errors (4xx)
            HttpURLConnection.HTTP_BAD_REQUEST -> ApiException.HttpException.ClientException.BadRequestException(
                statusMessage = statusMessage
            )
            HttpURLConnection.HTTP_UNAUTHORIZED -> ApiException.HttpException.ClientException.UnauthorizedException(
                statusMessage = statusMessage
            )
            HttpURLConnection.HTTP_FORBIDDEN -> ApiException.HttpException.ClientException.ForbiddenException(
                statusMessage = statusMessage
            )
            HttpURLConnection.HTTP_NOT_FOUND -> ApiException.HttpException.ClientException.NotFoundException(
                statusMessage = statusMessage
            )
            429 -> ApiException.HttpException.ClientException.TooManyRequestsException(
                statusMessage = statusMessage
            )
            in HttpURLConnection.HTTP_BAD_REQUEST..499 -> ApiException.HttpException.ClientException.GenericClientException(
                statusCode = statusCode,
                statusMessage = statusMessage
            )

            // Server errors (5xx)
            HttpURLConnection.HTTP_INTERNAL_ERROR -> ApiException.HttpException.ServerException.InternalServerErrorException(
                statusMessage = statusMessage
            )
            HttpURLConnection.HTTP_BAD_GATEWAY -> ApiException.HttpException.ServerException.BadGatewayException(
                statusMessage = statusMessage
            )
            HttpURLConnection.HTTP_UNAVAILABLE -> ApiException.HttpException.ServerException.ServiceUnavailableException(
                statusMessage = statusMessage
            )
            HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> ApiException.HttpException.ServerException.GatewayTimeoutException(
                statusMessage = statusMessage
            )
            in 500..599 -> ApiException.HttpException.ServerException.GenericServerException(
                statusCode = statusCode,
                statusMessage = statusMessage
            )

            // Unknown status code
            else -> ApiException.UnknownException(
                message = "Unknown HTTP error: $statusCode ${statusMessage ?: ""}",
                cause = null
            )
        }
    }
}
