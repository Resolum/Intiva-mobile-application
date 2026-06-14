package com.resolum.intiva.features.finances.data.remote.services

import com.resolum.intiva.features.finances.data.remote.models.CreateSpendingLimitRequestDto
import com.resolum.intiva.features.finances.data.remote.models.SpendingLimitResponseDto
import com.resolum.intiva.features.finances.data.remote.models.UpdateSpendingLimitAmountRequestDto
import com.resolum.intiva.features.finances.data.remote.models.UpdateSpendingLimitPeriodRequestDto
import com.resolum.intiva.features.shared.data.remote.models.ResponseWrapperDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for spending limit related API endpoints.
 */
interface SpendingLimitService {

    @POST("spending-limits")
    suspend fun createSpendingLimit(
        @Body request: CreateSpendingLimitRequestDto
    ): SpendingLimitResponseDto

    @GET("spending-limits/{spendingLimitId}")
    suspend fun getSpendingLimitById(
        @Path("spendingLimitId") spendingLimitId: Long
    ): SpendingLimitResponseDto

    @GET("spending-limits")
    suspend fun getSpendingLimits(
        @Query("ownerId") ownerId: Long,
        @Query("ownerType") ownerType: String? = null,
        @Query("targetType") targetType: String? = null,
        @Query("targetId") targetId: Long? = null
    ): ResponseWrapperDto<List<SpendingLimitResponseDto>>

    @PATCH("spending-limits/{spendingLimitId}/amount")
    suspend fun updateSpendingLimitAmount(
        @Path("spendingLimitId") spendingLimitId: Long,
        @Body request: UpdateSpendingLimitAmountRequestDto
    ): SpendingLimitResponseDto

    @PATCH("spending-limits/{spendingLimitId}/period")
    suspend fun updateSpendingLimitPeriod(
        @Path("spendingLimitId") spendingLimitId: Long,
        @Body request: UpdateSpendingLimitPeriodRequestDto
    ): SpendingLimitResponseDto

    @PATCH("spending-limits/{spendingLimitId}/activate")
    suspend fun activateSpendingLimit(
        @Path("spendingLimitId") spendingLimitId: Long
    ): SpendingLimitResponseDto

    @PATCH("spending-limits/{spendingLimitId}/deactivate")
    suspend fun deactivateSpendingLimit(
        @Path("spendingLimitId") spendingLimitId: Long
    ): SpendingLimitResponseDto
}
