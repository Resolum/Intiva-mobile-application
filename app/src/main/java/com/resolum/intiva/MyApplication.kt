package com.resolum.intiva

import android.app.Application
import com.resolum.intiva.core.fcm.notifications.DefaultNotificationChannelManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * MyApplication is the base class for maintaining global application state. It is annotated with @HiltAndroidApp to enable Hilt's dependency injection throughout the app.
 */
@HiltAndroidApp
class MyApplication : Application() {

    @Inject
    lateinit var defaultNotificationChannelManager: DefaultNotificationChannelManager

    override fun onCreate() {
        super.onCreate()
        defaultNotificationChannelManager.createDefaultChannel()
    }
}
