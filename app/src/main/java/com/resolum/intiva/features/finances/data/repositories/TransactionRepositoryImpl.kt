package com.resolum.intiva.features.finances.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.ConnectivityChecker
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.data.local.dao.PendingTransactionDao
import com.resolum.intiva.features.finances.data.local.dao.TransactionDao
import com.resolum.intiva.features.finances.data.local.mappers.toEntities
import com.resolum.intiva.features.finances.data.local.mappers.toAccountType
import com.resolum.intiva.features.finances.data.local.mappers.toDomainTransaction
import com.resolum.intiva.features.finances.data.local.mappers.toPendingEntity
import com.resolum.intiva.features.finances.data.local.mappers.toTransaction
import com.resolum.intiva.features.finances.data.local.mappers.toTransactionGroups
import com.resolum.intiva.features.finances.data.remote.TransactionFacadeService
import com.resolum.intiva.features.finances.data.remote.mappers.toDomain
import com.resolum.intiva.features.finances.data.sync.SyncItemResult
import com.resolum.intiva.features.finances.data.sync.TransactionOutboxSyncer
import com.resolum.intiva.features.finances.data.sync.TransactionSyncScheduler
import com.resolum.intiva.features.finances.domain.models.RegisterTransactionRequest
import com.resolum.intiva.features.finances.domain.models.SyncStatus
import com.resolum.intiva.features.finances.domain.models.SyncStatusSummary
import com.resolum.intiva.features.finances.domain.models.Transaction
import com.resolum.intiva.features.finances.domain.models.TransactionGroupByDate
import com.resolum.intiva.features.finances.domain.repositories.TransactionRepository
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Implementation of the [TransactionRepository] interface that handles financial transaction-related operations.
 *
 * This repository interacts with the [TransactionFacadeService] to perform API calls related to transactions,
 * and uses the [SessionRepository] to retrieve user session information when needed.
 */
class TransactionRepositoryImpl @Inject constructor(
    private val transactionFacadeService: TransactionFacadeService,
    private val sessionRepository: SessionRepository,
    private val pendingTransactionDao: PendingTransactionDao,
    private val transactionDao: TransactionDao,
    private val connectivityChecker: ConnectivityChecker,
    private val transactionOutboxSyncer: TransactionOutboxSyncer,
    private val transactionSyncScheduler: TransactionSyncScheduler
) : BaseRepository(), TransactionRepository {

    /**
     * Registers an individual financial transaction by making an API call through the [TransactionFacadeService].
     *
     * @param request The [RegisterTransactionRequest] containing the details of the transaction to be registered.
     * @return A [NetworkResult] containing the registered [Transaction] if successful, or an error if not.
     */
    override suspend fun registerIndividualTransaction(request: RegisterTransactionRequest): NetworkResult<Transaction> {
        return safeCall {
            val userId = sessionRepository.getUserId()
                ?: throw IllegalStateException("User ID not found in session")

            val pendingTransaction = request.toPendingEntity(
                userId = userId,
                accountType = request.ownerType.toAccountType()
            )
            val pendingId = pendingTransactionDao.insert(pendingTransaction)
            val savedTransaction = pendingTransaction.copy(id = pendingId)

            transactionSyncScheduler.enqueue()

            if (!connectivityChecker.isConnected()) {
                return@safeCall savedTransaction.toDomainTransaction()
            }

            when (val syncResult = transactionOutboxSyncer.syncById(pendingId)) {
                SyncItemResult.Synced -> savedTransaction.toDomainTransaction()

                is SyncItemResult.FailedConflict -> {
                    throw IllegalStateException(syncResult.reason)
                }

                SyncItemResult.RetryLater -> savedTransaction.toDomainTransaction()
            }
        }
    }

    /**
     * Retrieves a list of transactions grouped by date for a specific owner by making an API call through the [TransactionFacadeService].
     *
     * @param transactionType The type of transactions to filter by (e.g., "income", "expense").
     * @return A [NetworkResult] containing a list of [TransactionGroupByDate] if successful, or an error if not.
     */
    override suspend fun getTransactionsByOwnerId(transactionType: String?): NetworkResult<List<TransactionGroupByDate>> {
        val userId = sessionRepository.getUserId()
            ?: return NetworkResult.Error("User ID not found in session")

        val localTransactions = transactionDao
            .getTransactions(ownerId = userId, transactionType = transactionType)
            .toTransactionGroups()

        if (!connectivityChecker.isConnected()) {
            return if (localTransactions.isNotEmpty()) {
                NetworkResult.Success(localTransactions)
            } else {
                NetworkResult.Error("No internet connection")
            }
        }

        val remoteResult = safeCall {
            val response = transactionFacadeService.getTransactionsByOwnerId(
                ownerId = userId,
                transactionType = transactionType
            )

            val remoteGroups = response.data.map { it.toDomain() }
            transactionDao.deleteByOwnerAndType(ownerId = userId, transactionType = transactionType)
            transactionDao.insertAll(remoteGroups.toEntities())

            transactionDao
                .getTransactions(ownerId = userId, transactionType = transactionType)
                .toTransactionGroups()
        }

        return when (remoteResult) {
            is NetworkResult.Success -> remoteResult
            is NetworkResult.Error -> {
                if (localTransactions.isNotEmpty()) {
                    NetworkResult.Success(localTransactions)
                } else {
                    remoteResult
                }
            }
        }
    }

    override suspend fun getTransactionById(id: Long): NetworkResult<Transaction> {
        val remoteResult = safeCall {
            transactionFacadeService.getTransactionById(id).toDomain()
        }

        return when (remoteResult) {
            is NetworkResult.Success -> remoteResult
            is NetworkResult.Error -> {
                transactionDao.getById(id)?.let { localTransaction ->
                    NetworkResult.Success(localTransaction.toTransaction())
                } ?: remoteResult
            }
        }
    }

    /**
     * Retrieves the latest transactions for a specific owner by making an API call through the [TransactionFacadeService].
     *
     * @return A [NetworkResult] containing a list of [TransactionGroupByDate] representing the latest transactions if successful, or an error if not.
     */
    override suspend fun getLastestTransactionsByOwnerId(): NetworkResult<List<TransactionGroupByDate>> {
        val userId = sessionRepository.getUserId()
            ?: return NetworkResult.Error("User ID not found in session")

        val localTransactions = transactionDao
            .getLatestTransactions(ownerId = userId, limit = LATEST_TRANSACTION_LIMIT)
            .toTransactionGroups()

        if (!connectivityChecker.isConnected()) {
            return if (localTransactions.isNotEmpty()) {
                NetworkResult.Success(localTransactions)
            } else {
                NetworkResult.Error("No internet connection")
            }
        }

        val remoteResult = safeCall {
            val response = transactionFacadeService.getLastestTransactionByOwnerId(
                ownerId = userId
            )

            val remoteGroups = response.data.map { it.toDomain() }
            transactionDao.insertAll(remoteGroups.toEntities())
            remoteGroups
        }

        return when (remoteResult) {
            is NetworkResult.Success -> remoteResult
            is NetworkResult.Error -> {
                if (localTransactions.isNotEmpty()) {
                    NetworkResult.Success(localTransactions)
                } else {
                    remoteResult
                }
            }
        }
    }

    override fun observeSyncStatusSummary(): Flow<SyncStatusSummary> {
        return combine(
            pendingTransactionDao.observeCountByStatus(SyncStatus.PENDING),
            pendingTransactionDao.observeCountByStatus(SyncStatus.FAILED),
            pendingTransactionDao.observeLatestConflictReason()
        ) { pendingCount, failedCount, latestConflictReason ->
            SyncStatusSummary(
                pendingCount = pendingCount,
                failedCount = failedCount,
                latestConflictReason = latestConflictReason
            )
        }
    }

    private companion object {
        const val LATEST_TRANSACTION_LIMIT = 10
    }

}
