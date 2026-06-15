package com.resolum.intiva.core.root

import android.content.Context
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.resolum.intiva.core.deeplink.DeepLinkData
import com.resolum.intiva.core.deeplink.PendingDeepLink
import com.resolum.intiva.core.fcm.permissions.NotificationPermissionEffect
import com.resolum.intiva.core.navigation.graph.AppNavGraph
import com.resolum.intiva.core.ui.theme.IntivaTheme
import com.resolum.intiva.features.household.presentation.invitation.InviteBottomSheet
import com.resolum.intiva.features.household.presentation.invite.InviteViewModel

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val inviteViewModel: InviteViewModel = hiltViewModel()

    var deepLinkInvite by remember { mutableStateOf<DeepLinkData?>(null) }

    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isFirstLaunch = prefs.getBoolean("first_launch", true)

        if (isFirstLaunch) {
            val installId = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
            if (installId != null) {
                inviteViewModel.loadDeferredInvitation(installId)
            }
            prefs.edit().putBoolean("first_launch", false).apply()
        }
    }

    LaunchedEffect(Unit) {
        if (PendingDeepLink.data != null) {
            deepLinkInvite = PendingDeepLink.data
            PendingDeepLink.data = null
        }
    }

    LaunchedEffect(inviteViewModel.uiState.value.deferredState) {
        val state = inviteViewModel.uiState.value.deferredState
        if (state is com.resolum.intiva.core.common.state.UiState.Success) {
            deepLinkInvite = state.data
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

    deepLinkInvite?.let { invite ->
        InviteBottomSheet(
            token = invite.token,
            viewModel = inviteViewModel,
            groupName = invite.groupName,
            inviterName = invite.inviterName,
            onDismiss = { deepLinkInvite = null },
            onAccepted = { deepLinkInvite = null },
            onDeclined = { deepLinkInvite = null }
        )
    }
}
