package com.resolum.intiva.features.household.data.remote.services

import com.resolum.intiva.features.household.data.remote.models.CreateFamilyRequestDto
import com.resolum.intiva.features.household.data.remote.models.FamilyResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FamilyService {

    @POST("users/{userId}/group-families")
    suspend fun createFamily(
        @Path("userId") userId: Long,
        @Body body: CreateFamilyRequestDto
    ): FamilyResponseDto

    @GET("users/{userId}/group-families/{id}")
    suspend fun getFamilyById(
        @Path("userId") userId: Long,
        @Path("id") id: Long
    ): FamilyResponseDto
}
