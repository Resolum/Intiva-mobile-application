package com.resolum.intiva.features.communications.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.fcm.providers.FcmTokenProvider
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.communications.data.remote.models.NotificationDeviceResponseDto
import com.resolum.intiva.features.communications.data.remote.models.RegisterNotificationDeviceRequestDto
import com.resolum.intiva.features.communications.data.remote.services.NotificationDeviceService
import com.resolum.intiva.features.communications.domain.repositories.NotificationDeviceRepository
import javax.inject.Inject

private const val ANDROID_PLATFORM = "ANDROID"

class NotificationDeviceRepositoryImpl @Inject constructor(
    private val notificationDeviceService: NotificationDeviceService,
    private val tokenProvider: FcmTokenProvider
) : BaseRepository(), NotificationDeviceRepository {

    override suspend fun registerCurrentDevice(
        userId: Long
    ): NetworkResult<NotificationDeviceResponseDto> {
        val fcmToken = tokenProvider.getToken()
            ?: return NetworkResult.Error("Cannot get FCM token")

        return registerDeviceToken(userId, fcmToken)
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
