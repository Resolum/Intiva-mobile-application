package com.resolum.intiva.features.finances.data.sync

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
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

        Log.i(TAG, "Pending transactions count=${pendingTransactions.size}")

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
                idempotencyKey = transaction.idempotencyKey,
                request = transaction.toRegisterTransactionRequestDto()
            )
        }.getOrElse {
            Log.w(TAG, "Sync exception id=${transaction.id}: ${it.message}", it)
            return SyncItemResult.RetryLater
        }

        Log.i(TAG, "Sync response id=${transaction.id} http=${response.code()}")

        return when {
            response.isSuccessful -> {
                pendingTransactionDao.updateStatus(transaction.id, SyncStatus.SYNCED)
                Log.i(TAG, "Marked SYNCED id=${transaction.id}")
                SyncItemResult.Synced
            }

            response.code() == CONFLICT_STATUS_CODE && transaction.accountType == AccountType.FAMILY -> {
                val reason = response.errorBody()?.string().toConflictReason()
                pendingTransactionDao.markFailed(
                    id = transaction.id,
                    reason = reason
                )
                Log.i(TAG, "Marked FAILED conflict id=${transaction.id} reason=$reason")
                SyncItemResult.FailedConflict(reason)
            }

            response.code() in CLIENT_ERROR_STATUS_CODES -> {
                val reason = response.errorBody()?.string().toSyncFailureReason(response.code())
                pendingTransactionDao.markFailed(
                    id = transaction.id,
                    reason = reason
                )
                Log.i(TAG, "Marked FAILED client id=${transaction.id} reason=$reason")
                SyncItemResult.FailedConflict(reason)
            }

            else -> {
                Log.i(TAG, "Retry later id=${transaction.id} http=${response.code()}")
                SyncItemResult.RetryLater
            }
        }
    }

    private fun String?.toConflictReason(): String {
        return extractBackendMessage(DEFAULT_CONFLICT_REASON)
    }

    private fun String?.toSyncFailureReason(statusCode: Int): String {
        return extractBackendMessage("No se pudo sincronizar la transaccion. HTTP $statusCode")
    }

    private fun String?.extractBackendMessage(defaultMessage: String): String {
        if (isNullOrBlank()) return defaultMessage

        val responseMessage = runCatching {
            Gson().fromJson(this, ResponseDto::class.java)?.message
        }.getOrNull()

        if (!responseMessage.isNullOrBlank()) return responseMessage

        return runCatching {
            val json = JsonParser.parseString(this)
            json.findMessage()
        }.getOrNull()?.takeIf { it.isNotBlank() } ?: defaultMessage
    }

    private fun JsonElement.findMessage(): String? {
        if (isJsonPrimitive && asJsonPrimitive.isString) return asString
        if (!isJsonObject) return null

        val jsonObject = asJsonObject
        val directMessage = MESSAGE_KEYS.firstNotNullOfOrNull { key ->
            jsonObject.get(key)?.takeIf { it.isJsonPrimitive && it.asJsonPrimitive.isString }?.asString
        }
        if (!directMessage.isNullOrBlank()) return directMessage

        val firstNestedMessage = jsonObject.entrySet().firstNotNullOfOrNull { (_, value) ->
            when {
                value.isJsonArray -> value.asJsonArray.firstOrNull()?.findMessage()
                value.isJsonObject -> value.findMessage()
                value.isJsonPrimitive && value.asJsonPrimitive.isString -> value.asString
                else -> null
            }
        }

        return firstNestedMessage
    }

    private companion object {
        const val TAG = "TransactionSync"
        const val CONFLICT_STATUS_CODE = 409
        const val DEFAULT_CONFLICT_REASON = "No se pudo sincronizar por un conflicto con la cuenta familiar."
        val CLIENT_ERROR_STATUS_CODES = 400..499
        val MESSAGE_KEYS = listOf("message", "error", "detail", "details", "reason")
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
