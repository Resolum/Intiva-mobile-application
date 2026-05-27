package com.resolum.intiva.features.finances.data.remote.services

import com.resolum.intiva.features.finances.data.remote.models.RegisterTransactionRequestDto
import com.resolum.intiva.features.finances.data.remote.models.TransactionResponseDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit service interface for transaction-related API calls.
 * This interface defines the endpoints for managing financial transactions, such as registering a new transaction.
 */
interface TransactionService {

    /**
     * Makes a POST request to the "users/{userId}/transactions" endpoint to register a new financial transaction for a specific user.
     *
     * @param userId The ID of the user for whom the transaction is being registered.
     * @param body The request body containing the details of the transaction to be registered.
     * @return A [TransactionResponseDto] containing the details of the registered transaction if successful, or an error response if not.
     */
    @POST("users/{userId}/transactions")
    suspend fun registerIndividualTransaction(
        @Path("userId") userId: Long,
        @Body body: RegisterTransactionRequestDto,
    ) : TransactionResponseDto
}