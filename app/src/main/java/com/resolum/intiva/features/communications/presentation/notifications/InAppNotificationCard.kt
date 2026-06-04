package com.resolum.intiva.features.communications.presentation.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.resolum.intiva.core.ui.theme.IntivaColors

@Composable
fun InAppNotificationCard(
    notification: InAppNotificationUiState,
    modifier: Modifier = Modifier,
    onClick: (InAppNotificationUiState) -> Unit = {}
) {
    val style = notification.type.visualStyle()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(notification) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = IntivaColors.SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(style.backgroundColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = style.icon,
                    contentDescription = null,
                    tint = style.contentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        modifier = Modifier.weight(1f),
                        color = IntivaColors.TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(IntivaColors.PrimaryBrand, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    color = IntivaColors.TextSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = notification.createdAt,
                    color = IntivaColors.TextSecondary,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private data class NotificationVisualStyle(
    val icon: ImageVector,
    val backgroundColor: Color,
    val contentColor: Color
)

private fun InAppNotificationType.visualStyle(): NotificationVisualStyle =
    when (this) {
        InAppNotificationType.Info -> NotificationVisualStyle(
            icon = Icons.Default.NotificationsNone,
            backgroundColor = IntivaColors.PrimaryBrand.copy(alpha = 0.12f),
            contentColor = IntivaColors.PrimaryBrand
        )

        InAppNotificationType.Warning -> NotificationVisualStyle(
            icon = Icons.Default.Warning,
            backgroundColor = IntivaColors.StatusWarning.copy(alpha = 0.14f),
            contentColor = IntivaColors.StatusWarning
        )

        InAppNotificationType.Success -> NotificationVisualStyle(
            icon = Icons.Default.CheckCircle,
            backgroundColor = IntivaColors.StatusSuccess.copy(alpha = 0.14f),
            contentColor = IntivaColors.StatusSuccess
        )

        InAppNotificationType.Error -> NotificationVisualStyle(
            icon = Icons.Default.ErrorOutline,
            backgroundColor = IntivaColors.StatusError.copy(alpha = 0.14f),
            contentColor = IntivaColors.StatusError
        )
    }
