package com.resolum.intiva.core.network.model

/**
 * A generic wrapper for network responses.
 *
 * Use [NetworkResult.Success] when the call succeeds and [NetworkResult.Error] otherwise.
 */
sealed class NetworkResult<out T> {

    /** Represents a successful network response containing the expected data. */
    data class Success<T>(val data: T) : NetworkResult<T>()

    /** Represents a failed network response, containing an error message and optional details. */
    data class Error(
        val message: String,
        val code: Int? = null,
        val throwable: Throwable? = null,
    ) : NetworkResult<Nothing>()
}

/** Transforms the data inside a [NetworkResult.Success], leaving errors unchanged. */
inline fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> =
    when (this) {
        is NetworkResult.Success -> NetworkResult.Success(transform(data))
        is NetworkResult.Error -> this
    }
