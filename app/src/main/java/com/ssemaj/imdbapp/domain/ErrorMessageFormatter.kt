package com.ssemaj.imdbapp.domain

import android.content.Context
import com.ssemaj.imdbapp.R
import com.ssemaj.imdbapp.data.api.exception.ApiException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Formats ApiException into user-friendly error messages.
 */
@Singleton
internal class ErrorMessageFormatter @Inject constructor(
    private val context: Context
) {

    fun format(exception: ApiException): String {
        return when (exception) {
            is ApiException.NetworkException.NoConnectionException ->
                context.getString(R.string.error_no_connection)
            is ApiException.NetworkException.TimeoutException ->
                context.getString(R.string.error_timeout)
            is ApiException.NetworkException.GenericNetworkException ->
                context.getString(R.string.error_network_generic)
            is ApiException.HttpException.ClientException.UnauthorizedException ->
                context.getString(R.string.error_unauthorized)
            is ApiException.HttpException.ClientException.ForbiddenException ->
                context.getString(R.string.error_forbidden)
            is ApiException.HttpException.ClientException.NotFoundException ->
                context.getString(R.string.error_not_found)
            is ApiException.HttpException.ClientException.TooManyRequestsException ->
                context.getString(R.string.error_too_many_requests)
            is ApiException.HttpException.ClientException ->
                context.getString(R.string.error_client_generic, exception.message ?: "")
            is ApiException.HttpException.ServerException.InternalServerErrorException ->
                context.getString(R.string.error_server_internal)
            is ApiException.HttpException.ServerException.ServiceUnavailableException ->
                context.getString(R.string.error_server_unavailable)
            is ApiException.HttpException.ServerException ->
                context.getString(R.string.error_server_generic)
            is ApiException.SerializationException ->
                context.getString(R.string.error_serialization)
            is ApiException.UnknownException ->
                context.getString(R.string.error_unknown)
        }
    }
}
