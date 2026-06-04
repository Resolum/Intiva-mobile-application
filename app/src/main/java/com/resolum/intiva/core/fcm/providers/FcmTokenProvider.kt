package com.resolum.intiva.core.fcm.providers

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

private const val FCM_TOKEN_LOG_TAG = "FCM_TOKEN"

class FcmTokenProvider @Inject constructor() {

    suspend fun getToken(): String? = suspendCancellableCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!continuation.isActive) return@addOnCompleteListener
                val token = task.takeIf { it.isSuccessful }?.result
                Log.d(FCM_TOKEN_LOG_TAG, "Current FCM token: $token")
                continuation.resume(token)
            }
    }
}
