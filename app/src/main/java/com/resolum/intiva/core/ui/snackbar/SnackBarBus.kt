package com.resolum.intiva.core.ui.snackbar

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/** A simple event bus for sending snackbar messages across the app. */
object SnackBarBus {

    private val _messages = MutableSharedFlow<String>(replay = 0)
    val messages = _messages.asSharedFlow()

    suspend fun send(message: String) {
        _messages.emit(message)
    }
}