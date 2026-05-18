package com.resolum.intiva.core.ui.snackbar


import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.collectLatest

/** A composable that hosts a Snackbar and listens for messages from [SnackBarBus]. */
@Composable
fun IntivaSnackBarHost() {

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        SnackBarBus.messages.collect { message ->
            snackBarHostState.showSnackbar(message)
        }
    }

    SnackbarHost(hostState = snackBarHostState)
}