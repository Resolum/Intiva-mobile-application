package com.resolum.intiva.features.finances.data.sync

import com.google.gson.Gson
import com.resolum.intiva.features.finances.data.local.dao.PendingTransactionDao
import com.resolum.intiva.features.finances.data.local.entities.PendingTransactionEntity
import com.resolum.intiva.features.finances.data.local.mappers.toRegisterTransactionRequestDto
import com.resolum.intiva.features.finances.data.remote.TransactionFacadeService
import com.resolum.intiva.features.finances.domain.models.AccountType
import com.resolum.intiva.features.finances.domain.models.SyncStatus
import com.resolum.intiva.features.shared.data.remote.models.ResponseDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionOutboxSyncer @Inject constructor(
    private val pendingTransactionDao: PendingTransactionDao,
    private val transactionFacadeService: TransactionFacadeService
) {
    suspend fun syncPending(): SyncBatchResult {
        val pendingTransactions = pendingTransactionDao.getByStatus(SyncStatus.PENDING)
        var syncedCount = 0

        pendingTransactions.forEach { transaction ->
            when (sync(transaction)) {
                SyncItemResult.Synced -> syncedCount++
                is SyncItemResult.FailedConflict -> Unit
                SyncItemResult.RetryLater -> return SyncBatchResult.RetryLater(syncedCount)
            }
        }

        return SyncBatchResult.Completed(syncedCount)
    }

    suspend fun syncById(id: Long): SyncItemResult {
        val transaction = pendingTransactionDao.getById(id) ?: return SyncItemResult.Synced
        return sync(transaction)
    }

    private suspend fun sync(transaction: PendingTransactionEntity): SyncItemResult {
        val response = runCatching {
            transactionFacadeService.registerIndividualTransactionResponse(
                userId = transaction.userId,
                request = transaction.toRegisterTransactionRequestDto()
            )
        }.getOrElse {
            return SyncItemResult.RetryLater
        }

        return when {
            response.isSuccessful -> {
                pendingTransactionDao.updateStatus(transaction.id, SyncStatus.SYNCED)
                SyncItemResult.Synced
            }

            response.code() == CONFLICT_STATUS_CODE && transaction.accountType == AccountType.FAMILY -> {
                val reason = response.errorBody()?.string().toConflictReason()
                pendingTransactionDao.markFailed(
                    id = transaction.id,
                    reason = reason
                )
                SyncItemResult.FailedConflict(reason)
            }

            else -> {
                SyncItemResult.RetryLater
            }
        }
    }

    private fun String?.toConflictReason(): String {
        if (isNullOrBlank()) return DEFAULT_CONFLICT_REASON

        return runCatching {
            Gson().fromJson(this, ResponseDto::class.java)?.message
        }.getOrNull()?.takeIf { it.isNotBlank() } ?: DEFAULT_CONFLICT_REASON
    }

    private companion object {
        const val CONFLICT_STATUS_CODE = 409
        const val DEFAULT_CONFLICT_REASON = "No se pudo sincronizar por un conflicto con la cuenta familiar."
    }
}

sealed interface SyncItemResult {
    data object Synced : SyncItemResult
    data object RetryLater : SyncItemResult
    data class FailedConflict(val reason: String) : SyncItemResult
}

sealed interface SyncBatchResult {
    data class Completed(val syncedCount: Int) : SyncBatchResult
    data class RetryLater(val syncedCount: Int) : SyncBatchResult
}
