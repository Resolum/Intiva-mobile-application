package com.resolum.intiva.features.paymentmethodsandcategories.data.remote.services

import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.FinancialAccountResponseDto
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.CreateFinancialAccountRequestDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit service interface for fetching financial account data from the API.
 *
 * This interface defines the endpoints and HTTP methods for interacting with financial account-related
 * resources on the server. It includes a method to retrieve financial accounts based on a user's ID.
 */
interface FinancialAccountService {

    /**
     * Fetches a list of financial accounts associated with a specific user ID.
     *
     * @param userId The ID of the user whose financial accounts are to be retrieved.
     * @return A list of [FinancialAccountResponseDto] objects representing the user's financial accounts.
     */
    @GET("users/{userId}/financial-accounts")
    suspend fun getFinancialAccountsByUserId(
        @Path("userId") userId: Long
    ) : List<FinancialAccountResponseDto>

    @POST("users/{userId}/financial-accounts")
    suspend fun createFinancialAccount(
        @Path("userId") userId: Long,
        @Body request: CreateFinancialAccountRequestDto
    ): FinancialAccountResponseDto
}