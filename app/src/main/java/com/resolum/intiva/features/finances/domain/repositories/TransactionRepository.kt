package com.resolum.intiva.features.finances.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.domain.models.RegisterTransactionRequest
import com.resolum.intiva.features.finances.domain.models.Transaction

/**
 * Repository interface for managing financial transactions.
 *
 * This interface defines the contract for operations related to financial transactions, such as registering a new transaction.
 * Implementations of this interface will handle the actual data operations, such as making network requests or accessing a local database.
 */
interface TransactionRepository {

    /**
     * Registers a new financial transaction based on the provided request data.
     *
     * @param request The [RegisterTransactionRequest] containing the details of the transaction to be registered.
     * @return A [NetworkResult] containing the registered [Transaction] if successful, or an error if the operation fails.
     */
    suspend fun registerIndividualTransaction(request: RegisterTransactionRequest) : NetworkResult<Transaction>
}