package com.resolum.intiva.features.finances.presentation.spendinglimits.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.finances.presentation.spendinglimits.SpendingLimitSummary
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.components.CategoryIcon
import java.math.BigDecimal
import java.text.DecimalFormat

@Composable
fun SpendingLimitListItem(
    summary: SpendingLimitSummary,
    category: Category?,
    onAdjustClick: () -> Unit
) {
    val progressColor = when {
        summary.isExceeded -> IntivaColors.StatusError
        summary.progressPercent >= 80 -> Color(0xFFFF9800)
        else -> IntivaColors.CheckGreen
    }
    val statusText = when {
        summary.isExceeded -> "Límite alcanzado"
        summary.progressPercent >= 80 -> "¡Cerca del límite!"
        else -> "Dentro del límite"
    }
    val categoryColor = parseColor(category?.color, fallback = progressColor.copy(alpha = 0.14f))
    val iconTint = parseColor(category?.color, fallback = progressColor)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (summary.isExceeded) {
                    Modifier.border(
                        width = 1.dp,
                        color = IntivaColors.StatusError.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(18.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(categoryColor.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    CategoryIcon(
                        iconName = category?.icon ?: "category",
                        tintColor = iconTint
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = category?.name ?: "Categoría ${summary.limit.targetId}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = IntivaColors.TextPrimary
                    )
                    Text(
                        text = statusText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = progressColor
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatCurrency(summary.limit.spentAmount),
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (summary.isExceeded) IntivaColors.StatusError else IntivaColors.TextPrimary
                    )
                    Text(
                        text = "de ${formatCurrency(summary.limit.limitAmount)}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = IntivaColors.TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            LinearProgressIndicator(
                progress = { summary.progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = progressColor,
                trackColor = Color(0xFFE5E0EC)
            )

            Spacer(modifier = Modifier.height(14.dp))
            OutlinedButton(
                onClick = onAdjustClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (summary.isExceeded) IntivaColors.PrimaryBrand else Color.White,
                    contentColor = if (summary.isExceeded) IntivaColors.TextInverse else IntivaColors.PrimaryBrand
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = IntivaColors.PrimaryBrand
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ajustar límite",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun parseColor(value: String?, fallback: Color): Color =
    runCatching { Color(value?.toColorInt() ?: return@runCatching fallback) }.getOrElse { fallback }

private fun formatCurrency(amount: BigDecimal): String {
    val formatter = DecimalFormat("#,##0.00")
    return "S/. ${formatter.format(amount)}"
}
