package com.resolum.intiva.features.communications.data.repositories

import android.util.Log
import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.fcm.providers.FcmTokenProvider
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.communications.data.remote.models.NotificationDeviceResponseDto
import com.resolum.intiva.features.communications.data.remote.models.RegisterNotificationDeviceRequestDto
import com.resolum.intiva.features.communications.data.remote.services.NotificationDeviceService
import com.resolum.intiva.features.communications.domain.repositories.NotificationDeviceRepository
import javax.inject.Inject

private const val ANDROID_PLATFORM = "ANDROID"
private const val FCM_DEVICE_LOG_TAG = "FCM_DEVICE"

class NotificationDeviceRepositoryImpl @Inject constructor(
    private val notificationDeviceService: NotificationDeviceService,
    private val tokenProvider: FcmTokenProvider
) : BaseRepository(), NotificationDeviceRepository {

    override suspend fun registerCurrentDevice(
        userId: Long
    ): NetworkResult<NotificationDeviceResponseDto> {
        val fcmToken = tokenProvider.getToken()
            ?: return NetworkResult.Error("Cannot get FCM token").also {
                Log.w(FCM_DEVICE_LOG_TAG, "Cannot register notification device. FCM token is null.")
            }

        return registerDeviceToken(userId, fcmToken).also { result ->
            when (result) {
                is NetworkResult.Success -> Log.i(
                    FCM_DEVICE_LOG_TAG,
                    "Notification device registered. userId=$userId deviceId=${result.data.id}"
                )
                is NetworkResult.Error -> Log.w(
                    FCM_DEVICE_LOG_TAG,
                    "Notification device registration failed. userId=$userId code=${result.code} message=${result.message}",
                    result.throwable
                )
            }
        }
    }

    override suspend fun registerDeviceToken(
        userId: Long,
        deviceToken: String
    ): NetworkResult<NotificationDeviceResponseDto> = safeCall {
        notificationDeviceService.registerNotificationDevice(
            RegisterNotificationDeviceRequestDto(
                userId = userId,
                deviceToken = deviceToken,
                platform = ANDROID_PLATFORM
            )
        )
    }
}
