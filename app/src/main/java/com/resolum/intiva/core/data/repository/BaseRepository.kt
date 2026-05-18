package com.resolum.intiva.core.data.repository

import com.google.gson.Gson
import java.io.IOException
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.shared.data.remote.models.ResponseDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

/**
 * BaseRepository provides common functionality for all repositories, including:
 * - Safe network calls with error handling
 * - Network-bound resource management (fetching from local cache and remote source)
 *
 * Repositories can extend this class to leverage these features without duplicating code.
 */
abstract class BaseRepository {

    /** Gson instance for parsing error responses. */
    private val gson = Gson()

    /**
     * Executes a network call and emits the result as a Flow of NetworkResult.
     * On success, emits NetworkResult.Success with the data.
     * On failure, emits NetworkResult.Error with the error message and throwable.
     */
    protected fun <T> networkFlow(
        call: suspend () -> T,
    ): Flow<NetworkResult<T>> = flow<NetworkResult<T>> {
        emit(NetworkResult.Success(call()))
    }.catch { emit(NetworkResult.Error(it.message ?: "Error", throwable = it)) }

    /**
     * Executes a network call and emits the result as a Flow of NetworkResult.
     * On success, emits NetworkResult.Success with the data.
     * On failure, emits NetworkResult.Error with the error message and throwable.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun <Local, Remote> networkBoundFlow(
        queryLocal: () -> Flow<Local>,
        shouldFetch: (Local) -> Boolean,
        fetchRemote: suspend () -> Remote,
        saveRemote: suspend (Remote) -> Unit,
    ): Flow<NetworkResult<Local>> = flow<NetworkResult<Local>> {
        val local = queryLocal().first()
        emit(NetworkResult.Success(local))

        if (shouldFetch(local)) {
            try {
                val remote = fetchRemote()
                saveRemote(remote)
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message ?: "Network Error", throwable = e))
            }
        }
    }.flatMapLatest { seed ->
        queryLocal().map { NetworkResult.Success(it) }
    }

    /**
     * Executes a suspend function and wraps its result in a NetworkResult, handling exceptions.
     * On success, returns NetworkResult.Success with the data.
     * On failure, returns NetworkResult.Error with the error message and throwable.
     */
    protected suspend fun <T> safeCall(
        call: suspend () -> T,
    ): NetworkResult<T> = try {
        NetworkResult.Success(call())

    } catch (e: HttpException) {

        val errorJson = try {
            e.response()?.errorBody()?.source()?.buffer?.clone()?.readUtf8()
        } catch (ex: Exception) {
            null
        }

        val message = try {
            errorJson?.let {
                gson.fromJson(it, ResponseDto::class.java)?.message
            }
        } catch (parseError: Exception) {
            null
        }

        NetworkResult.Error(
            message = message ?: "HTTP ${e.code()}",
            code = e.code(),
            throwable = e
        )
    } catch (e: IOException) {

        NetworkResult.Error(
            message = "No internet connection",
            throwable = e
        )

    } catch (e: Exception) {

        NetworkResult.Error(
            message = e.message ?: "Unexpected error",
            throwable = e
        )
    }
}