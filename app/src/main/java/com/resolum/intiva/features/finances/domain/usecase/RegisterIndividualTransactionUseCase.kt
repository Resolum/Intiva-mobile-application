package com.resolum.intiva.features.finances.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.domain.models.RegisterTransactionRequest
import com.resolum.intiva.features.finances.domain.models.Transaction
import com.resolum.intiva.features.finances.domain.models.TransactionType
import com.resolum.intiva.features.finances.domain.repositories.TransactionRepository
import jakarta.inject.Inject
import java.math.BigDecimal

/**
 * Use case for registering an individual financial transaction.
 *
 * This use case validates the input data and interacts with the TransactionRepository
 * to perform the registration of the transaction.
 *
 * @property transactionRepository The repository responsible for handling transaction data operations.
 */
class RegisterIndividualTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    /**
     * Registers an individual financial transaction based on the provided request data.
     *
     * @param request The data required to register the transaction.
     * @return A NetworkResult containing the registered Transaction or an error message.
     * @throws IllegalArgumentException If the input data is invalid.
     */
    suspend operator fun invoke(
        request: RegisterTransactionRequest
    ): NetworkResult<Transaction> {

        if (request.amount <= BigDecimal.ZERO) {
            return NetworkResult.Error("Amount must be greater than zero")
        }

        if (request.transactionType !in TransactionType.entries) {
            return NetworkResult.Error("Invalid transaction type")
        }

        return transactionRepository.registerIndividualTransaction(request)
    }
}