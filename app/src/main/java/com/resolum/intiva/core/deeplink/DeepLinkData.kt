package com.resolum.intiva.core.deeplink

data class DeepLinkData(
    val token: String,
    val groupName: String = "",
    val inviterName: String = ""
)
