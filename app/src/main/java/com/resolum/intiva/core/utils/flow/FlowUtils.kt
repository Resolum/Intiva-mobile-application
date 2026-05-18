package com.resolum.intiva.core.utils.flow

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.network.model.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Maps a [Flow] of [NetworkResult] to a [Flow] of [UiState].
 * Automatically emits [UiState.Loading] on start, [UiState.Success] on success,
 * and [UiState.Error] on failure or exception.
 */
fun <T> Flow<NetworkResult<T>>.toUiState(): Flow<UiState<T>> =
    map { result ->
        when (result) {
            is NetworkResult.Success -> UiState.Success(result.data)
            is NetworkResult.Error -> UiState.Error(result.message, result.throwable)
        }
    }
        .onStart { emit(UiState.Loading) }
        .catch { emit(UiState.Error(it.message ?: "Unknown error", it)) }
