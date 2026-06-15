package com.resolum.intiva.features.household.domain.models

data class QrCodeResult(
    val qrBase64: String,
    val invitationLink: String,
    val token: String,
    val expiresAt: String
)
