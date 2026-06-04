package com.resolum.intiva.core.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

/** A data class that implements [SnackbarVisuals] and includes a [SnackBarType] to specify the type of snackbar. */
data class SnackBarVisualsWithType(
    override val message: String,
    val type: SnackBarType,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short
) : SnackbarVisuals