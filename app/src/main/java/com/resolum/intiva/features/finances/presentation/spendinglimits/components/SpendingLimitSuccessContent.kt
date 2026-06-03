package com.resolum.intiva.features.finances.presentation.spendinglimits.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.finances.presentation.spendinglimits.SpendingLimitSummary
import java.math.BigDecimal
import java.text.DecimalFormat

@Composable
fun SpendingLimitSuccessContent(summary: SpendingLimitSummary) {
    val progressColor = when {
        summary.isExceeded -> IntivaColors.StatusError
        summary.progressPercent >= 80 -> IntivaColors.StatusWarning
        else -> IntivaColors.PrimaryGreen
    }
    val subtitle = if (summary.isExceeded) {
        "Has excedido el límite por ${formatCurrency(summary.limit.spentAmount - summary.limit.limitAmount)}"
    } else {
        "Te quedan ${formatCurrency(summary.remainingAmount)} disponibles"
    }

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(48.dp)
        ) {
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFFF2F0FA),
                strokeWidth = 5.dp
            )
            CircularProgressIndicator(
                progress = { summary.progressFraction },
                modifier = Modifier.fillMaxSize(),
                color = progressColor,
                strokeWidth = 5.dp,
                trackColor = Color.Transparent
            )
            Text(
                text = "${summary.progressPercent}%",
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = IntivaColors.TextPrimary
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Límite de Gasto Mensual",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = IntivaColors.TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = if (summary.isExceeded) IntivaColors.StatusError else IntivaColors.TextSecondary
            )
        }
    }
}


private fun formatCurrency(amount: BigDecimal): String {
    val formatter = DecimalFormat("#,##0.00")
    return "S/ ${formatter.format(amount)}"
}