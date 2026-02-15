package com.ssemaj.imdbapp.http

import com.ssemaj.imdbapp.data.api.ApiResult
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Factory for creating ApiResultCallAdapter instances.
 * Handles suspend functions that return ApiResult<T>.
 */
internal class ApiResultCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // Check if return type is ApiResult
        if (getRawType(returnType) != ApiResult::class.java) {
            return null
        }

        // Check if it's a ParameterizedType (ApiResult<T>)
        val type = returnType as? ParameterizedType
            ?: throw IllegalArgumentException("ApiResult return type must be parameterized")

        // Get the inner type (T)
        val responseType = getParameterUpperBound(0, type)

        // Create and return the adapter
        return ApiResultCallAdapter<Any>(responseType) as CallAdapter<*, *>
    }
}
