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


    suspend fun getTransactionsByOwnerId(ownerId: Long, transactionType: String?) = transactionService.getTransactionsByOwnerId(ownerId, transactionType)

}