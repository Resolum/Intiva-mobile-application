package com.resolum.intiva.features.profiles.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.profiles.domain.models.Profile
import java.io.File

interface ProfileRepository {
    suspend fun getProfile(userId: Long): NetworkResult<Profile>
    suspend fun updateProfile(userId: Long, name: String, bio: String, phoneNumber: String): NetworkResult<Profile>
    suspend fun updateAvatar(userId: Long, imageFile: File): NetworkResult<Profile>
}
