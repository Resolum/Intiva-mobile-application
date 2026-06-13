package com.resolum.intiva

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.resolum.intiva.core.deeplink.DeepLinkData
import com.resolum.intiva.core.deeplink.PendingDeepLink
import com.resolum.intiva.core.root.AppRoot
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleIntent(intent)
        setContent {
            AppRoot()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val data = intent.data ?: return

        if (data.scheme == "miapp" && data.host == "invite") {
            val token = data.getQueryParameter("token") ?: return
            val group = data.getQueryParameter("group") ?: ""
            val inviter = data.getQueryParameter("inviter") ?: ""
            PendingDeepLink.data = DeepLinkData(
                token = token,
                groupName = group,
                inviterName = inviter
            )
        }
    }
}
