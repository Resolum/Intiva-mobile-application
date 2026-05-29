package com.resolum.intiva.features.finances.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.domain.models.TransactionGroupByDate
import com.resolum.intiva.features.finances.domain.repositories.TransactionRepository
import jakarta.inject.Inject

/**
 * Use case for retrieving transactions grouped by date for a specific owner.
 *
 * This use case interacts with the [TransactionRepository] to fetch the transactions based on the provided transaction type.
 * It returns a [NetworkResult] containing a list of [TransactionGroupByDate] if successful, or an error if the operation fails.
 */
class GetTransactionsByOwnerIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    /**
     * Invokes the use case to retrieve transactions grouped by date for a specific owner.
     *
     * @param transactionType An optional string representing the type of transactions to filter by (e.g., "income", "expense"). If null, all transactions will be retrieved.
     * @return A [NetworkResult] containing a list of [TransactionGroupByDate] if successful, or an error if the operation fails.
     */
    suspend operator fun invoke(
        transactionType: String? = null,
        onlyLatest: Boolean = false
    ): NetworkResult<List<TransactionGroupByDate>> {

        return if (onlyLatest) {
            transactionRepository.getLastestTransactionsByOwnerId()
        } else {
            transactionRepository.getTransactionsByOwnerId(transactionType)
        }
    }
}