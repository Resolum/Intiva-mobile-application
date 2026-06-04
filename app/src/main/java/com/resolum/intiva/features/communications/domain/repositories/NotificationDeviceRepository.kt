package com.resolum.intiva.features.communications.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.communications.data.remote.models.NotificationDeviceResponseDto

interface NotificationDeviceRepository {

    suspend fun registerCurrentDevice(userId: Long): NetworkResult<NotificationDeviceResponseDto>

    suspend fun registerDeviceToken(
        userId: Long,
        deviceToken: String
    ): NetworkResult<NotificationDeviceResponseDto>
}
