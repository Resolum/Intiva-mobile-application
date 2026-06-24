package com.resolum.intiva.features.communications.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.communications.domain.models.InAppNotification

interface InAppNotificationRepository {
    suspend fun getNotifications(userId: Long): NetworkResult<List<InAppNotification>>
}
