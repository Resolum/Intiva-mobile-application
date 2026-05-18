package com.resolum.intiva.core.network.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT_SECONDS = 30L

/**
 * Factory that builds a [Retrofit] instance with a shared [OkHttpClient].
 * The OkHttpClient is configured with standard timeouts and can include any number of interceptors.
 * The Gson converter is used for JSON serialization/deserialization and can be customized if needed.
 * Provided via Hilt — see [com.resolum.intiva.core.di.NetworkModule].
 */
object ApiClientFactory {

    /**
     * Builds a Retrofit instance with the specified base URL, OkHttpClient, and Gson converter.
     * The Gson converter is optional and defaults to a standard Gson instance if not provided.
     */
    fun buildRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        gson: Gson = buildGson(),
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    /**
     * Builds an OkHttpClient with the specified interceptors and standard timeouts.
     * Interceptors are added in the order they are provided.
     */
    fun buildOkHttpClient(vararg interceptors: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .apply { interceptors.forEach { addInterceptor(it) } }
            .build()

    /** Builds a default Gson instance. Customize this method if you need specific serialization settings. */
    private fun buildGson(): Gson = GsonBuilder().create()
}
