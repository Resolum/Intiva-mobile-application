package com.resolum.intiva.core.ui.snackbar

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SnackBarBus {

    private val _messages = MutableSharedFlow<SnackBarVisualsWithType>(
        extraBufferCapacity = 1
    )

    val messages = _messages.asSharedFlow()

    fun send(message: String, type: SnackBarType = SnackBarType.Normal) {
        _messages.tryEmit(
            SnackBarVisualsWithType(
                message = message,
                type = type,
                actionLabel = type.name
            )
        )
    }
}