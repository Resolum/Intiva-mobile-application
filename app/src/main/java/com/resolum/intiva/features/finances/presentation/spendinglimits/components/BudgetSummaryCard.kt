package com.resolum.intiva.features.finances.presentation.spendinglimits.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.finances.presentation.spendinglimits.SpendingLimitSummary
import com.resolum.intiva.features.finances.presentation.spendinglimits.daysUntilMonthEnd
import com.resolum.intiva.features.finances.presentation.spendinglimits.formatCurrency
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun BudgetSummaryCard(summaries: List<SpendingLimitSummary>) {
    val totalLimit = summaries.fold(BigDecimal.ZERO) { total, item -> total + item.limit.limitAmount }
    val totalSpent = summaries.fold(BigDecimal.ZERO) { total, item -> total + item.limit.spentAmount }
    val available = (totalLimit - totalSpent).max(BigDecimal.ZERO)
    val progress = if (totalLimit > BigDecimal.ZERO) {
        totalSpent.divide(totalLimit, 4, java.math.RoundingMode.HALF_UP).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "PRESUPUESTO TOTAL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = IntivaColors.TextSecondary
                    )
                    Text(
                        text = formatCurrency(totalLimit),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = IntivaColors.PrimaryBrand
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "DISPONIBLE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = IntivaColors.TextSecondary
                    )
                    Text(
                        text = formatCurrency(available),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = IntivaColors.TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = IntivaColors.PrimaryBrand,
                trackColor = Color(0xFFE0DCE8)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${(progress * 100).toInt()}% utilizado",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = IntivaColors.TextSecondary
                )
                Text(
                    text = "Restan ${daysUntilMonthEnd()} días",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = IntivaColors.TextSecondary
                )
            }
        }
    }
}

private fun daysUntilMonthEnd(): Long {
    val today = LocalDate.now()
    return ChronoUnit.DAYS.between(today, today.withDayOfMonth(today.lengthOfMonth())).coerceAtLeast(0)
}