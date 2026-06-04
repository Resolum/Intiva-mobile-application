package com.resolum.intiva.features.communications.data.remote.services

import com.resolum.intiva.features.communications.data.remote.models.NotificationDeviceResponseDto
import com.resolum.intiva.features.communications.data.remote.models.RegisterNotificationDeviceRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationDeviceService {

    @POST("notification-devices")
    suspend fun registerNotificationDevice(
        @Body request: RegisterNotificationDeviceRequestDto
    ): NotificationDeviceResponseDto
}
