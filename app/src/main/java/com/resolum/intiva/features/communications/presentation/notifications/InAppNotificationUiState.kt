package com.resolum.intiva.features.communications.presentation.notifications

import com.resolum.intiva.features.communications.domain.models.InAppNotification
import com.resolum.intiva.features.communications.domain.models.InAppNotificationStatus
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

sealed class InAppNotificationsScreenState {
    data object Loading : InAppNotificationsScreenState()
    data class Success(
        val groupedNotifications: Map<String, List<InAppNotificationUiState>>
    ) : InAppNotificationsScreenState()
    data class Error(val message: String) : InAppNotificationsScreenState()
}

data class InAppNotificationUiState(
    val id: Long,
    val title: String,
    val message: String,
    val type: InAppNotificationType = InAppNotificationType.Info,
    val createdAt: String,
    val isRead: Boolean = false
)

enum class InAppNotificationType {
    Info,
    Warning,
    Success,
    Error,
    Urgent,
    Remembering
}

fun InAppNotification.toNotificationType(): InAppNotificationType = when (type.lowercase()) {
    "info" -> InAppNotificationType.Info
    "warning" -> InAppNotificationType.Warning
    "success" -> InAppNotificationType.Success
    "error" -> InAppNotificationType.Error
    "urgent" -> InAppNotificationType.Urgent
    "remembering" -> InAppNotificationType.Remembering
    else -> InAppNotificationType.Info
}

fun InAppNotification.toUiState(): InAppNotificationUiState = InAppNotificationUiState(
    id = id,
    title = title,
    message = message,
    type = toNotificationType(),
    createdAt = formatRelativeTime(createdAt),
    isRead = status == InAppNotificationStatus.READ
)

private fun formatRelativeTime(createdAt: LocalDateTime): String {
    val now = LocalDateTime.now()
    val duration = Duration.between(createdAt, now)
    return when {
        duration.toMinutes() < 1 -> "Ahora"
        duration.toMinutes() < 60 -> "Hace ${duration.toMinutes()} min"
        duration.toHours() < 24 -> "Hace ${duration.toHours()} h"
        duration.toDays() == 1L -> "Ayer"
        duration.toDays() < 7 -> "Hace ${duration.toDays()} días"
        else -> {
            val formatter = DateTimeFormatter.ofPattern("d MMM", Locale("es"))
            createdAt.format(formatter)
        }
    }
}
