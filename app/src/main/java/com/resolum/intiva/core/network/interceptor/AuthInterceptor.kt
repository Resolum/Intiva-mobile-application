package com.resolum.intiva.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp interceptor that appends an Authorization header to every outgoing request.
 *
 * The token is retrieved lazily via [tokenProvider], allowing it to be refreshed
 * after login without recreating the OkHttpClient.
 *
 * @param tokenProvider Lambda returning the current Bearer token (without the "Bearer " prefix).
 */
class AuthInterceptor(
    private val tokenProvider: () -> String?,
) : Interceptor {

    /**
     * Intercepts outgoing HTTP requests and adds the Authorization header if a token is available.
     * The token is retrieved from the provided [tokenProvider] lambda, allowing for dynamic
     * token retrieval (e.g., from a secure storage or in-memory cache) without needing to
     * recreate the OkHttpClient when the token changes.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider()
        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrBlank()) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()
        return chain.proceed(request)
    }
}
