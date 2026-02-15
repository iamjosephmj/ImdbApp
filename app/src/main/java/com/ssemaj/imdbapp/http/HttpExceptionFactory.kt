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
    fun createHttpException(statusCode: Int, statusMessage: String?): ApiException = when {
        // Client errors (4xx)
        statusCode == HttpURLConnection.HTTP_BAD_REQUEST -> 
            ApiException.HttpException.ClientException.BadRequestException(statusMessage = statusMessage)
        
        statusCode == HttpURLConnection.HTTP_UNAUTHORIZED -> 
            ApiException.HttpException.ClientException.UnauthorizedException(statusMessage = statusMessage)
        
        statusCode == HttpURLConnection.HTTP_FORBIDDEN -> 
            ApiException.HttpException.ClientException.ForbiddenException(statusMessage = statusMessage)
        
        statusCode == HttpURLConnection.HTTP_NOT_FOUND -> 
            ApiException.HttpException.ClientException.NotFoundException(statusMessage = statusMessage)
        
        statusCode == 429 -> 
            ApiException.HttpException.ClientException.TooManyRequestsException(statusMessage = statusMessage)
        
        statusCode in HttpURLConnection.HTTP_BAD_REQUEST..499 -> 
            ApiException.HttpException.ClientException.GenericClientException(
                statusCode = statusCode,
                statusMessage = statusMessage
            )

        // Server errors (5xx)
        statusCode == HttpURLConnection.HTTP_INTERNAL_ERROR -> 
            ApiException.HttpException.ServerException.InternalServerErrorException(statusMessage = statusMessage)
        
        statusCode == HttpURLConnection.HTTP_BAD_GATEWAY -> 
            ApiException.HttpException.ServerException.BadGatewayException(statusMessage = statusMessage)
        
        statusCode == HttpURLConnection.HTTP_UNAVAILABLE -> 
            ApiException.HttpException.ServerException.ServiceUnavailableException(statusMessage = statusMessage)
        
        statusCode == HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> 
            ApiException.HttpException.ServerException.GatewayTimeoutException(statusMessage = statusMessage)
        
        statusCode in 500..599 -> 
            ApiException.HttpException.ServerException.GenericServerException(
                statusCode = statusCode,
                statusMessage = statusMessage
            )

        // Unknown status code
        else -> statusMessage?.let { msg ->
            ApiException.UnknownException(
                message = buildString { 
                    append("Unknown HTTP error: ").append(statusCode).append(" ").append(msg)
                },
                cause = null
            )
        } ?: ApiException.UnknownException(
            message = "Unknown HTTP error: $statusCode",
            cause = null
        )
    }
}
