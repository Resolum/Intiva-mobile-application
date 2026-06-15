package com.resolum.intiva.features.household.domain.models

data class Family(
    val id: Long,
    val name: String,
    val description: String,
    val status: String,
    val ownerId: Long,
    val membersActive: Int,
    val createdAt: String
)
