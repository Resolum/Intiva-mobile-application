package com.resolum.intiva.core.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.util.concurrent.TimeUnit

/**
 * OkHttp interceptor that logs detailed information about HTTP requests and responses.
 *
 * This interceptor logs the request URL, method, body (if present), response code,
 * response time, and response body. It is designed to be used in debug builds to
 * help developers understand the network interactions of their app.
 *
 * @param isDebug Flag indicating whether logging should be enabled. Set to false in production.
 */
class LoggingInterceptor(
    private val isDebug: Boolean = true
) : Interceptor {

    /**
     * Intercepts HTTP requests and responses, logging detailed information about them.
     *
     * If [isDebug] is false, the interceptor simply proceeds with the request without logging.
     * Otherwise, it logs the request URL, method, body (if present), response code,
     * response time, and response body. It also handles exceptions that may occur during
     * the request and logs them as network errors.
     *
     * @param chain The OkHttp interceptor chain.
     * @return The HTTP response after logging.
     * @throws Exception If an error occurs during the request, it is rethrown after logging.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (!isDebug) return chain.proceed(request)

        val startNs = System.nanoTime()

        Log.d("HttpLogger", "──── REQUEST ────")
        Log.d("HttpLogger", "URL: ${request.url}")
        Log.d("HttpLogger", "METHOD: ${request.method}")

        request.headers.let { headers ->
            Log.d("HttpLogger", "HEADERS:")
            headers.forEach { (name, value) ->
                if (name.equals("Authorization", ignoreCase = true)) {
                    Log.d("HttpLogger", "  $name: Bearer ${value.take(20)}...")
                } else {
                    Log.d("HttpLogger", "  $name: $value")
                }
            }
        }

        request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            val charset = body.contentType()?.charset(Charsets.UTF_8) ?: Charsets.UTF_8
            Log.d("HttpLogger", "BODY: ${buffer.readString(charset)}")
        }

        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            Log.e("HttpLogger", "NETWORK ERROR: ${e.message}", e)
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBodyString = try {
            response.peekBody(Long.MAX_VALUE).string()
        } catch (e: Exception) {
            "Unable to read body"
        }

        Log.d("HttpLogger", "──── RESPONSE ────")
        Log.d("HttpLogger", "CODE: ${response.code}")
        Log.d("HttpLogger", "TIME: ${tookMs}ms")
        Log.d("HttpLogger", "BODY: $responseBodyString")

        return response
    }
}