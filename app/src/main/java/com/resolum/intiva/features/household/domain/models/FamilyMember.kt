package com.resolum.intiva.features.household.domain.models

data class FamilyMember(
    val id: Long,
    val userId: Long,
    val familyId: Long,
    val role: String,
    val status: String,
    val joinedAt: String
)
