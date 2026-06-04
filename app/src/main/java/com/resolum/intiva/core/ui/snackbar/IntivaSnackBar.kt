package com.resolum.intiva.core.ui.snackbar

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** A custom SnackbarHost that displays snack bars with different background colors based on their type (success, error, normal). */
@Composable
fun IntivaSnackBarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState = hostState) { data ->
        val visuals = data.visuals as? SnackBarVisualsWithType

        val backgroundColor = when (visuals?.type) {
            SnackBarType.Success -> Color(0xFF3B6D11)
            SnackBarType.Error   -> Color(0xFFA32D2D)
            else                 -> Color(0xFF2C2C2A)
        }

        Snackbar(
            snackbarData = data,
            containerColor = backgroundColor,
            contentColor = Color.White
        )
    }
}