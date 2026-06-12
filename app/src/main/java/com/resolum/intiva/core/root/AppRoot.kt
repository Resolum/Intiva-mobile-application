package com.resolum.intiva.core.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.resolum.intiva.core.fcm.permissions.NotificationPermissionEffect
import com.resolum.intiva.core.navigation.graph.AppNavGraph
import com.resolum.intiva.core.ui.theme.IntivaTheme

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val activity = context as? android.app.Activity
        val uri = activity?.intent?.data
        if (uri != null) {
            val token = uri.getQueryParameter("token")
            if (token != null) {
                navController.navigate("invitation/$token") {
                    launchSingleTop = true
                }
            }
        }
    }

    IntivaTheme {
        NotificationPermissionEffect()

        Box(modifier = Modifier.fillMaxSize()) {
            AppNavGraph(navController = navController)

            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
