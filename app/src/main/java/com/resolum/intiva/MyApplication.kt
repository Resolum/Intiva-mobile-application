package com.resolum.intiva

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.resolum.intiva.core.fcm.notifications.DefaultNotificationChannelManager
import com.resolum.intiva.features.finances.data.sync.TransactionSyncScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * MyApplication is the base class for maintaining global application state. It is annotated with @HiltAndroidApp to enable Hilt's dependency injection throughout the app.
 */
@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var defaultNotificationChannelManager: DefaultNotificationChannelManager

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var transactionSyncScheduler: TransactionSyncScheduler

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        defaultNotificationChannelManager.createDefaultChannel()
        transactionSyncScheduler.enqueue()
    }
}
