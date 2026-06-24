package com.resolum.intiva.features.paymentmethodsandcategories.data.remote.services

import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.CreateFinancialAccountRequestDto
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.FinancialAccountResponseDto
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.UpdateFinancialAccountRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface FinancialAccountService {

    @GET("users/{userId}/financial-accounts")
    suspend fun getFinancialAccountsByUserId(
        @Path("userId") userId: Long
    ): List<FinancialAccountResponseDto>

    @POST("users/{userId}/financial-accounts")
    suspend fun createFinancialAccount(
        @Path("userId") userId: Long,
        @Body request: CreateFinancialAccountRequestDto
    ): FinancialAccountResponseDto

    @PATCH("users/{userId}/financial-accounts/{accountId}")
    suspend fun updateFinancialAccount(
        @Path("userId") userId: Long,
        @Path("accountId") accountId: Long,
        @Body request: UpdateFinancialAccountRequestDto
    ): FinancialAccountResponseDto
}
