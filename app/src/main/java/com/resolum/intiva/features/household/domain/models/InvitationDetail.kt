package com.resolum.intiva.features.household.domain.models

data class InvitationDetail(
    val id: Long,
    val token: String,
    val status: String,
    val invitedByName: String,
    val familyId: Long,
    val isExpired: Boolean
)
