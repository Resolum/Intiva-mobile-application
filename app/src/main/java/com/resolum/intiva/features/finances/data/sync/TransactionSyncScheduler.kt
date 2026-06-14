package com.resolum.intiva.features.finances.data.sync

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionSyncScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    fun enqueue() {
        val request = OneTimeWorkRequestBuilder<TransactionSyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        Log.i(TAG, "Enqueue transaction sync work id=${request.id}")

        WorkManager.getInstance(context).enqueueUniqueWork(
            WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    private companion object {
        const val TAG = "TransactionSync"
        const val WORK_NAME = "pending_transaction_sync"
    }
}
