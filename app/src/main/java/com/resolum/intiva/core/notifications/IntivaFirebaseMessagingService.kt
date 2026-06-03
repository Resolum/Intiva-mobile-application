package com.resolum.intiva.core.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Service used by Firebase Cloud Messaging to deliver token updates and incoming messages.
 */
class IntivaFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: Send this token to the backend when the notification endpoint is available.
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // TODO: Show a local notification for foreground/data messages.
    }
}
