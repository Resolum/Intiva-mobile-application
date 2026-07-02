package com.resolum.intiva.features.finances.data.remote.services

import com.resolum.intiva.features.finances.data.remote.models.RegisterTransactionRequestDto
import com.resolum.intiva.features.finances.data.remote.models.TransactionGroupByDateResponseDto
import com.resolum.intiva.features.finances.data.remote.models.TransactionResponseDto
import com.resolum.intiva.features.finances.data.remote.models.TransactionWithDesignResponseDto
import com.resolum.intiva.features.shared.data.remote.models.ResponseWrapperDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response

/**
 * Retrofit service interface for transaction-related API calls.
 * This interface defines the endpoints for managing financial transactions, such as registering a new transaction.
 */
interface TransactionService {

    /**
     * Makes a POST request to the "transactions" endpoint to register a new financial transaction.
     *
     * @param idempotencyKey Unique key used by the backend to safely deduplicate retries.
     * @param body The request body containing the details of the transaction to be registered.
     * @return A [Response] with the HTTP status of the transaction registration operation.
     */
    @POST("transactions")
    suspend fun registerIndividualTransaction(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body body: RegisterTransactionRequestDto,
    ) : Response<Unit>

    /**
     * Makes a GET request to the "transactions" endpoint to retrieve a list of transactions for a specific owner, optionally filtered by transaction type.
     *
     * @param ownerId The ID of the owner whose transactions are being retrieved.
     * @param transactionType An optional query parameter to filter transactions by type (e.g., "income", "expense").
     * @return A [ResponseWrapperDto] containing a list of [TransactionGroupByDateResponseDto] objects representing the transactions grouped by date, or an error response if the request fails.
     */
    @GET("transactions")
    suspend fun getTransactionsByOwnerId(
        @Query("ownerId") ownerId: Long,
        @Query("transactionType") transactionType: String? = null,
    ) : ResponseWrapperDto<List<TransactionGroupByDateResponseDto>>

    @GET("transactions/{id}")
    suspend fun getTransactionById(
        @Path("id") id: Long,
    ) : TransactionResponseDto

    /**
     * Makes a GET request to the "transactions/lastest" endpoint to retrieve the latest transactions for a specific owner.
     *
     * @param ownerId The ID of the owner whose latest transactions are being retrieved.
     * @return A [ResponseWrapperDto] containing a list of [TransactionGroupByDateResponseDto] objects representing the latest transactions grouped by date, or an error response if the request fails.
     */
    @GET("transactions/lastest")
    suspend fun getLastestTransactionByOwnerId(
        @Query("ownerId") ownerId: Long,
    ) : ResponseWrapperDto<List<TransactionGroupByDateResponseDto>>

}
