package com.resolum.intiva.features.household.domain.models

data class Invitation(
    val id: Long,
    val token: String,
    val status: String,
    val sentAt: String,
    val expiresAt: String,
    val respondedAt: String?,
    val invitedBy: Long,
    val familyId: Long,
    val userInvitedId: Long,
    val isExpired: Boolean
)
