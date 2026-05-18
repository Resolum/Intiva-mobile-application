package com.resolum.intiva.core.root

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.resolum.intiva.core.navigation.AppNavGraph
import com.resolum.intiva.core.ui.snackbar.IntivaSnackBarHost

@Composable
fun AppRoot() {
    val navController = rememberNavController()

    Scaffold(
        snackbarHost = { IntivaSnackBarHost() },
    ) { padding ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(padding),
        )
    }
}