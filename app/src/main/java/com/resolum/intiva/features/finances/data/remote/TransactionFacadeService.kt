package com.resolum.intiva.features.finances.data.remote

import com.resolum.intiva.features.finances.data.remote.models.RegisterTransactionRequestDto
import com.resolum.intiva.features.finances.data.remote.services.TransactionService
import javax.inject.Inject

/**
 * Facade service for financial transaction-related operations.
 *
 * This service abstracts the underlying transaction mechanisms and provides a simple interface
 * for the rest of the application to interact with. It handles registering individual transactions
 * by delegating to the appropriate services.
 */
class TransactionFacadeService @Inject constructor(
    private val transactionService: TransactionService
) {

    /**
     * Registers an individual financial transaction for a user with the provided [RegisterTransactionRequestDto].
     *
     * @param request The request containing the details of the transaction to be registered.
     * @param userId The ID of the user performing the transaction.
     * @return The result of the transaction registration operation.
     */
    suspend fun registerIndividualTransaction(request: RegisterTransactionRequestDto, userId: Long)  = transactionService.registerIndividualTransaction(userId, request)

    suspend fun registerIndividualTransactionResponse(
        request: RegisterTransactionRequestDto,
        userId: Long
    ) = transactionService.registerIndividualTransaction(userId, request)

    /**
     * Retrieves a list of transactions for a specific owner, optionally filtered by transaction type.
     *
     * @param ownerId The ID of the owner whose transactions are being retrieved.
     * @param transactionType An optional parameter to filter transactions by type (e.g., "income", "expense").
     * @return A list of transactions grouped by date for the specified owner and transaction type.
     */
    suspend fun getTransactionsByOwnerId(ownerId: Long, transactionType: String?) = transactionService.getTransactionsByOwnerId(ownerId, transactionType)

    suspend fun getTransactionById(id: Long) = transactionService.getTransactionById(id)

    /**
     * Retrieves the latest transactions for a specific owner.
     *
     * @param ownerId The ID of the owner whose latest transactions are being retrieved.
     * @return A list of the latest transactions grouped by date for the specified owner.
     */
    suspend fun getLastestTransactionByOwnerId(ownerId: Long) = transactionService.getLastestTransactionByOwnerId(ownerId)

}
