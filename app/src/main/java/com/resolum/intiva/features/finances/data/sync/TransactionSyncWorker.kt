package com.resolum.intiva.features.finances.data.sync

import android.content.Context
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
        return when (transactionOutboxSyncer.syncPending()) {
            is SyncBatchResult.Completed -> Result.success()
            is SyncBatchResult.RetryLater -> Result.retry()
        }
    }
}
