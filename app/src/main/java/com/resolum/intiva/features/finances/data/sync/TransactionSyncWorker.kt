package com.resolum.intiva.features.finances.data.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TransactionSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val transactionOutboxSyncer: TransactionOutboxSyncer
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        Log.i(TAG, "Worker started id=$id attempt=$runAttemptCount")

        return when (val result = transactionOutboxSyncer.syncPending()) {
            is SyncBatchResult.Completed -> {
                Log.i(TAG, "Worker completed synced=${result.syncedCount}")
                Result.success()
            }
            is SyncBatchResult.RetryLater -> {
                Log.i(TAG, "Worker retry synced=${result.syncedCount}")
                Result.retry()
            }
        }
    }

    private companion object {
        const val TAG = "TransactionSync"
    }
}
