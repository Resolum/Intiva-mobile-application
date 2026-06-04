package com.resolum.intiva.features.finances.presentation.spendinglimits.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors

@Composable
fun SpendingLimitLoadingContent() {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = IntivaColors.IconPurple,
            strokeWidth = 5.dp
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Límite de Gasto Mensual",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = IntivaColors.TextPrimary
            )
            Text(
                text = "Cargando tu límite...",
                fontSize = 12.sp,
                color = IntivaColors.TextSecondary
            )
        }
    }
}