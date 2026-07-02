package com.resolum.intiva.features.finances.presentation.spendinglimits.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.finances.domain.models.SpendingLimit
import com.resolum.intiva.features.finances.presentation.spendinglimits.SpendingLimitFrequency
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdjustSpendingLimitSheet(
    limit: SpendingLimit,
    updateState: UiState<SpendingLimit>,
    onDismiss: () -> Unit,
    onSave: (String, SpendingLimitFrequency, Boolean) -> Unit
) {
    var amount by remember(limit.id) { mutableStateOf(formatAmount(limit.limitAmount)) }
    val initialFrequency = remember(limit.id, limit.startDate, limit.endDate) {
        inferFrequency(limit)
    }
    var frequency by remember(limit.id) { mutableStateOf(initialFrequency) }
    val isLoading = updateState is UiState.Loading

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = IntivaColors.BackgroundDefault
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 28.dp)
        ) {
            Text(
                text = "Ajustar límite",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = IntivaColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Actualiza el presupuesto y el periodo activo.",
                fontSize = 14.sp,
                color = IntivaColors.TextSecondary
            )
            Spacer(modifier = Modifier.height(22.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { value ->
                    amount = value.filter { it.isDigit() || it == '.' }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Monto máximo") },
                prefix = { Text("S/. ") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IntivaColors.PrimaryBrand,
                    focusedLabelColor = IntivaColors.PrimaryBrand,
                    cursorColor = IntivaColors.PrimaryBrand
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Frecuencia",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = IntivaColors.TextSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SpendingLimitFrequency.entries.forEach { option ->
                    FrequencyButton(
                        frequency = option,
                        selected = frequency == option,
                        onClick = { frequency = option },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (updateState is UiState.Error) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = updateState.message,
                    color = IntivaColors.StatusError,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onSave(amount, frequency, frequency != initialFrequency) },
                enabled = amount.isNotBlank() && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = IntivaColors.PrimaryBrand,
                    contentColor = IntivaColors.TextInverse,
                    disabledContainerColor = IntivaColors.BackgroundSurface,
                    disabledContentColor = IntivaColors.TextSecondary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = IntivaColors.TextInverse,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Guardar cambios",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

private fun formatAmount(amount: BigDecimal): String {
    return amount.stripTrailingZeros().toPlainString()
}

private fun inferFrequency(limit: SpendingLimit): SpendingLimitFrequency {
    val start = runCatching { LocalDate.parse(limit.startDate) }.getOrNull()
    val end = runCatching { LocalDate.parse(limit.endDate) }.getOrNull()
    if (start == null || end == null) return SpendingLimitFrequency.MONTHLY

    val inclusiveDays = ChronoUnit.DAYS.between(start, end) + 1
    return when {
        inclusiveDays <= 7 -> SpendingLimitFrequency.WEEKLY
        start.dayOfYear == 1 && end.dayOfYear == end.lengthOfYear() -> SpendingLimitFrequency.YEARLY
        else -> SpendingLimitFrequency.MONTHLY
    }
}
