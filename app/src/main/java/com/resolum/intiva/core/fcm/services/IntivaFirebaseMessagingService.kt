package com.resolum.intiva.core.fcm.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.resolum.intiva.core.fcm.notifications.FcmNotificationHelper
import com.resolum.intiva.features.communications.domain.repositories.NotificationDeviceRepository
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val FCM_TOKEN_LOG_TAG = "FCM_TOKEN"
private const val FCM_MESSAGE_LOG_TAG = "FCM_MESSAGE"

/**
 * Service used by Firebase Cloud Messaging to deliver token updates and incoming messages.
 */
@AndroidEntryPoint
class IntivaFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var sessionRepository: SessionRepository

    @Inject
    lateinit var notificationDeviceRepository: NotificationDeviceRepository

    @Inject
    lateinit var notificationHelper: FcmNotificationHelper

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(FCM_TOKEN_LOG_TAG, "New FCM token: $token")
        serviceScope.launch {
            val userId = sessionRepository.getUserId() ?: return@launch
            notificationDeviceRepository.registerDeviceToken(
                userId = userId,
                deviceToken = token
            )
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d(
            FCM_MESSAGE_LOG_TAG,
            "Message received. from=${message.from}, messageId=${message.messageId}, data=${message.data}"
        )

        val title = message.notification?.title
            ?: message.data["title"]
            ?: message.data["notificationTitle"]
            ?: "Intiva"

        val body = message.notification?.body
            ?: message.data["body"]
            ?: message.data["message"]
            ?: "Tienes una nueva notificacion"

        notificationHelper.showNotification(
            title = title,
            body = body,
            data = message.data
        )
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }
}
