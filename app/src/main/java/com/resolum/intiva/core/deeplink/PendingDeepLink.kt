package com.resolum.intiva.core.deeplink

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object PendingDeepLink {
    var data: DeepLinkData? by mutableStateOf(null)
}
