package com.resolum.intiva.features.finances.presentation.transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SyncProblem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.finances.domain.models.SyncStatusSummary

@Composable
fun SyncStatusBanner(
    summary: SyncStatusSummary,
    modifier: Modifier = Modifier
) {
    if (summary.pendingCount == 0 && summary.failedCount == 0) return

    val hasFailure = summary.failedCount > 0
    val backgroundColor = if (hasFailure) Color(0xFFFFF1F1) else Color(0xFFEFF7FF)
    val contentColor = if (hasFailure) Color(0xFFB3261E) else IntivaColors.PrimaryBrand
    val title = if (hasFailure) {
        "${summary.failedCount} operacion(es) no se pudieron sincronizar"
    } else {
        "${summary.pendingCount} operacion(es) pendientes de sincronizar"
    }
    val detail = if (hasFailure) {
        summary.latestConflictReason ?: "Revisa la operacion rechazada por el backend."
    } else {
        "Se enviaran automaticamente cuando vuelva la conexion."
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        androidx.compose.foundation.layout.Row {
            Icon(
                imageVector = if (hasFailure) Icons.Default.SyncProblem else Icons.Default.Sync,
                contentDescription = null,
                tint = contentColor
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = title,
                    color = contentColor,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = detail,
                    color = contentColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
