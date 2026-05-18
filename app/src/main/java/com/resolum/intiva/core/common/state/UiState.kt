package com.resolum.intiva.core.common.state

/**
 * Represents the possible states of a UI screen or component.
 *
 * Usage in ViewModel:
 * ```
 * private val _uiState = MutableStateFlow<UiState<MyData>>(UiState.Idle)
 * val uiState: StateFlow<UiState<MyData>> = _uiState.asStateFlow()
 * ```
 */
sealed class UiState<out T> {

    /** Initial state, no operation has been triggered yet. */
    data object Idle : UiState<Nothing>()

    /** A loading operation is in progress. */
    data object Loading : UiState<Nothing>()

    /** The operation completed successfully with [data]. */
    data class Success<T>(val data: T) : UiState<T>()

    /** The operation failed with the given [message] and optional [throwable]. */
    data class Error(
        val message: String,
        val throwable: Throwable? = null,
    ) : UiState<Nothing>()
}

/** Returns true only when this state is [UiState.Loading]. */
val UiState<*>.isLoading: Boolean get() = this is UiState.Loading

/** Returns the wrapped data or null if the state is not [UiState.Success]. */
fun <T> UiState<T>.dataOrNull(): T? = (this as? UiState.Success)?.data
