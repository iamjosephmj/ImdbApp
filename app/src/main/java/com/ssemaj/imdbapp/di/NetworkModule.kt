package com.ssemaj.imdbapp.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ssemaj.imdbapp.BuildConfig
import com.ssemaj.imdbapp.http.ApiResultCallAdapterFactory
import com.ssemaj.imdbapp.http.AuthInterceptor
import com.ssemaj.imdbapp.http.ErrorInterceptor
import com.ssemaj.imdbapp.data.api.TmdbService
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
internal class NetworkModule {

    @Provides
    @Singleton
    fun json(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun okHttp(
        errorInterceptor: ErrorInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(errorInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun retrofit(
        client: OkHttpClient,
        json: Json,
        apiResultCallAdapterFactory: ApiResultCallAdapterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.TMDB_BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .addCallAdapterFactory(apiResultCallAdapterFactory)
        .build()

    @Provides
    @Singleton
    fun apiResultCallAdapterFactory(): ApiResultCallAdapterFactory = ApiResultCallAdapterFactory()

    @Provides
    @Singleton
    fun tmdbService(retrofit: Retrofit): TmdbService = retrofit.create(TmdbService::class.java)
}
