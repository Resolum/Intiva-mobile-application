package com.resolum.intiva.features.communications.presentation.notifications

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
