package com.resolum.intiva.features.communications.data.remote.services

import com.resolum.intiva.features.communications.data.remote.models.InAppNotificationResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface InAppNotificationService {

    @GET("notifications")
    suspend fun getNotifications(
        @Query("userId") userId: Long
    ): InAppNotificationResponseDto
}
