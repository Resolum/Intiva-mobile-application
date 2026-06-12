package com.resolum.intiva.features.finances.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.domain.models.RegisterTransactionRequest
import com.resolum.intiva.features.finances.domain.models.Transaction
import com.resolum.intiva.features.finances.domain.models.TransactionGroupByDate
import com.resolum.intiva.features.finances.domain.models.TransactionType

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

    /**
     * Retrieves a list of transactions grouped by date for a specific owner.
     *
     * @return A [NetworkResult] containing a list of [TransactionGroupByDate] if successful, or an error if the operation fails.
     */
    suspend fun getTransactionsByOwnerId(transactionType: String?) : NetworkResult<List<TransactionGroupByDate>>

    suspend fun getTransactionById(id: Long): NetworkResult<Transaction>

    /**
     * Retrieves the latest transactions for a specific owner.
     *
        * @return A [NetworkResult] containing a list of [TransactionGroupByDate] representing the latest transactions if successful, or an error if the operation fails.
     */
    suspend fun getLastestTransactionsByOwnerId() : NetworkResult<List<TransactionGroupByDate>>
}
