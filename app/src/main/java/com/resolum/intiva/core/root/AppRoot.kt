package com.resolum.intiva.core.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.resolum.intiva.core.navigation.graph.AppNavGraph

/**
 * Root composable of the application. It sets up the main scaffold, including the navigation graph and global UI elements like the snackbar host.
 */
@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {
        AppNavGraph(navController = navController)

        SnackbarHost(
            hostState = snackBarHostState,
            modifier  = Modifier.align(Alignment.BottomCenter)
        )
    }
}