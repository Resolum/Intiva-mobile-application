package com.resolum.intiva.features.communications.presentation.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.resolum.intiva.core.ui.theme.IntivaColors

@Composable
fun NotificationSettingItem(
    item: NotificationSettingDisplayItem,
    onToggle: (itemId: String, enabled: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IntivaColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = IntivaColors.TextSecondary
            )
        }

        Switch(
            checked = item.enabled,
            onCheckedChange = { onToggle(item.id, it) },
            colors = SwitchDefaults.colors(
                checkedTrackColor = IntivaColors.PrimaryBrand,
                checkedThumbColor = Color.White,
                uncheckedTrackColor = IntivaColors.TextSecondary.copy(alpha = 0.3f),
                uncheckedThumbColor = Color.White
            )
        )
    }
}
