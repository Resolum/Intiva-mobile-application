package com.resolum.intiva.features.communications.presentation.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.resolum.intiva.core.ui.theme.IntivaColors

@Composable
fun InAppNotificationsContent(
    groupedNotifications: Map<String, List<InAppNotificationUiState>>,
    modifier: Modifier = Modifier,
    onNotificationClick: (InAppNotificationUiState) -> Unit = {}
) {
    if (groupedNotifications.isEmpty()) {
        EmptyNotificationsContent(modifier = modifier)
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        groupedNotifications.forEach { (dateGroup, notifications) ->
            stickyHeader {
                Text(
                    text = dateGroup,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(IntivaColors.BackgroundSurface)
                        .padding(vertical = 8.dp),
                    color = IntivaColors.TextSecondary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(
                items = notifications,
                key = { it.id }
            ) { notification ->
                InAppNotificationCard(
                    notification = notification,
                    onClick = onNotificationClick
                )
            }
        }
    }
}

@Composable
private fun EmptyNotificationsContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.NotificationsNone,
                contentDescription = null,
                tint = IntivaColors.TextSecondary
            )

            Text(
                text = "No tienes notificaciones",
                color = IntivaColors.TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Cuando Intiva te envie una alerta, aparecera aqui.",
                color = IntivaColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
