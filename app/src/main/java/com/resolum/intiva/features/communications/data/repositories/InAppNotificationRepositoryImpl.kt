package com.resolum.intiva.features.communications.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.communications.data.remote.models.InAppNotificationDto
import com.resolum.intiva.features.communications.data.remote.services.InAppNotificationService
import com.resolum.intiva.features.communications.domain.models.InAppNotification
import com.resolum.intiva.features.communications.domain.models.InAppNotificationStatus
import com.resolum.intiva.features.communications.domain.repositories.InAppNotificationRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class InAppNotificationRepositoryImpl @Inject constructor(
    private val service: InAppNotificationService
) : BaseRepository(), InAppNotificationRepository {

    override suspend fun getNotifications(userId: Long): NetworkResult<List<InAppNotification>> = safeCall {
        val response = service.getNotifications(userId)
        response.data.map { it.toDomain() }
    }

    private fun InAppNotificationDto.toDomain(): InAppNotification {
        return InAppNotification(
            id = id,
            recipientUserId = recipientUserId,
            type = type,
            source = source,
            sourceId = sourceId,
            title = title,
            message = message,
            status = when (status.uppercase()) {
                "READ" -> InAppNotificationStatus.READ
                else -> InAppNotificationStatus.UNREAD
            },
            createdAt = try {
                LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME)
            } catch (_: Exception) {
                LocalDateTime.now()
            }
        )
    }
}
