package com.resolum.intiva.core.fcm.notifications

import android.util.Log
import com.resolum.intiva.features.communications.domain.repositories.NotificationDeviceRepository
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import javax.inject.Inject
import javax.inject.Singleton

private const val FCM_DEVICE_LOG_TAG = "FCM_DEVICE"

@Singleton
class NotificationDeviceRegister @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val notificationDeviceRepository: NotificationDeviceRepository
) {
    suspend fun registerCurrentSessionDevice() {
        val userId = sessionRepository.getUserId()
        if (userId == null) {
            Log.d(FCM_DEVICE_LOG_TAG, "Skipping notification device registration. No active session.")
            return
        }

        notificationDeviceRepository.registerCurrentDevice(userId)
    }
}
