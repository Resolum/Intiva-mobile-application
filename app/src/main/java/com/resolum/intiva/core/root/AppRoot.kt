package com.resolum.intiva.core.root

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.resolum.intiva.core.ui.snackbar.IntivaSnackBarHost
import com.resolum.intiva.features.iam.presentation.SignUpScreen
import com.resolum.intiva.features.iam.presentation.TermsAndConditionsScreen

/** The root composable of the app, setting up the main scaffold and navigation. */
@Composable
fun AppRoot() {
    var showTermsAndConditions by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { IntivaSnackBarHost() }
    ) { padding ->

        if (showTermsAndConditions) {
            TermsAndConditionsScreen(
                onNavigateBack = {
                    showTermsAndConditions = false
                }
            )
        } else {
            SignUpScreen(
                modifier = Modifier.padding(padding),
                showBackButton = false,
                onNavigateToTermsAndConditions = {
                    showTermsAndConditions = true
                }
            ) {
            }
        }
    }
}