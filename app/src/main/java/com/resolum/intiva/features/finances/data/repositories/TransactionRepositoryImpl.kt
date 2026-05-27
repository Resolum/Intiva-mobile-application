package com.resolum.intiva.features.finances.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.data.remote.TransactionFacadeService
import com.resolum.intiva.features.finances.data.remote.mappers.toDomain
import com.resolum.intiva.features.finances.data.remote.mappers.toDto
import com.resolum.intiva.features.finances.domain.models.RegisterTransactionRequest
import com.resolum.intiva.features.finances.domain.models.Transaction
import com.resolum.intiva.features.finances.domain.repositories.TransactionRepository
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import javax.inject.Inject

/**
 * Implementation of the [TransactionRepository] interface that handles financial transaction-related operations.
 *
 * This repository interacts with the [TransactionFacadeService] to perform API calls related to transactions,
 * and uses the [SessionRepository] to retrieve user session information when needed.
 */
class TransactionRepositoryImpl @Inject constructor(
    private val transactionFacadeService: TransactionFacadeService,
    private val sessionRepository: SessionRepository
) : BaseRepository(), TransactionRepository {

    /**
     * Registers an individual financial transaction by making an API call through the [TransactionFacadeService].
     *
     * @param request The [RegisterTransactionRequest] containing the details of the transaction to be registered.
     * @return A [NetworkResult] containing the registered [Transaction] if successful, or an error if not.
     */
    override suspend fun registerIndividualTransaction(request: RegisterTransactionRequest): NetworkResult<Transaction> {
        val result = safeCall {

            val userId = sessionRepository.getUserId() ?: throw IllegalStateException("User ID not found in session")

            val requestDto = request.toDto(
                performedByUserId = userId
            )

            val response = transactionFacadeService.registerIndividualTransaction(
                userId = userId,
                request = requestDto
            )

            response.toDomain()
        }

        return result
    }
}