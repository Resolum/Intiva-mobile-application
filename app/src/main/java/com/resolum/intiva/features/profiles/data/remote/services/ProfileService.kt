package com.resolum.intiva.features.profiles.data.remote.services

import com.resolum.intiva.features.profiles.data.remote.models.ProfileResponseDto
import com.resolum.intiva.features.profiles.data.remote.models.UpdateProfileRequestDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileService {

    @GET("profiles/{userId}")
    suspend fun getProfile(@Path("userId") userId: Long): ProfileResponseDto

    @PUT("profiles/{userId}")
    suspend fun updateProfile(
        @Path("userId") userId: Long,
        @Body request: UpdateProfileRequestDto
    ): ProfileResponseDto

    @Multipart
    @PATCH("profiles/{userId}/avatar")
    suspend fun updateAvatar(
        @Path("userId") userId: Long,
        @Part file: MultipartBody.Part
    ): ProfileResponseDto
}
