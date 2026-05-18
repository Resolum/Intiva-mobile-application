package com.resolum.intiva.core.root

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.resolum.intiva.core.ui.snackbar.IntivaSnackBarHost
import com.resolum.intiva.features.iam.presentation.SignUpScreen

/** The root composable of the app, setting up the main scaffold and navigation. */
@Composable
fun AppRoot() {

    Scaffold(
        snackbarHost = { IntivaSnackBarHost() }
    ) { padding ->

        SignUpScreen(
            modifier = Modifier.padding(padding)
        ) {
        }
    }
}