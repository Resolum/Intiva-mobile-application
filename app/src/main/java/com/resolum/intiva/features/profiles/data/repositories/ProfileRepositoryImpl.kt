package com.resolum.intiva.features.profiles.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.profiles.data.remote.ProfileFacadeService
import com.resolum.intiva.features.profiles.data.remote.mappers.toDomain
import com.resolum.intiva.features.profiles.data.remote.models.UpdateProfileRequestDto
import com.resolum.intiva.features.profiles.domain.models.Profile
import com.resolum.intiva.features.profiles.domain.repositories.ProfileRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileFacadeService: ProfileFacadeService
) : BaseRepository(), ProfileRepository {

    override suspend fun getProfile(userId: Long): NetworkResult<Profile> = safeCall {
        profileFacadeService.getProfile(userId).toDomain()
    }

    override suspend fun updateProfile(
        userId: Long,
        name: String,
        bio: String,
        phoneNumber: String
    ): NetworkResult<Profile> = safeCall {
        val request = UpdateProfileRequestDto(name = name, bio = bio, phoneNumber = phoneNumber)
        profileFacadeService.updateProfile(userId, request).toDomain()
    }

    override suspend fun updateAvatar(userId: Long, imageFile: File): NetworkResult<Profile> = safeCall {
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
        profileFacadeService.updateAvatar(userId, body).toDomain()
    }
}
