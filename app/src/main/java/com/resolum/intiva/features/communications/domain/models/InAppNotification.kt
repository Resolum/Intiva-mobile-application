package com.resolum.intiva.features.communications.domain.models

import java.time.LocalDateTime

data class InAppNotification(
    val id: Long,
    val recipientUserId: Long,
    val type: String,
    val source: String,
    val sourceId: Long,
    val title: String,
    val message: String,
    val status: InAppNotificationStatus,
    val createdAt: LocalDateTime
)

enum class InAppNotificationStatus {
    READ,
    UNREAD
}
