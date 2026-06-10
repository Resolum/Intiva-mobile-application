package com.resolum.intiva.features.profiles.data.remote

import com.resolum.intiva.features.profiles.data.remote.models.UpdateProfileRequestDto
import com.resolum.intiva.features.profiles.data.remote.services.ProfileService
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfileFacadeService @Inject constructor(
    private val profileService: ProfileService
) {
    suspend fun getProfile(userId: Long) =
        profileService.getProfile(userId)

    suspend fun updateProfile(userId: Long, request: UpdateProfileRequestDto) =
        profileService.updateProfile(userId, request)

    suspend fun updateAvatar(userId: Long, file: MultipartBody.Part) =
        profileService.updateAvatar(userId, file)
}
