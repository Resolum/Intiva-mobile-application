package com.resolum.intiva.features.profiles.domain.models

data class Profile(
    val id: Long,
    val name: String,
    val avatarUrl: String,
    val email: String,
    val userId: Long,
    val phoneNumber: String,
    val bio: String,
    val age: Int
)
