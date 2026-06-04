package com.resolum.intiva.features.finances.presentation.spendinglimits.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.finances.presentation.spendinglimits.SpendingLimitSummary

@Composable
fun SpendingLimitCard(
    state: UiState<SpendingLimitSummary>,
    onRetry: () -> Unit,
    onOpenAlert: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenAlert() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        when (state) {
            is UiState.Loading -> SpendingLimitLoadingContent()
            is UiState.Success -> SpendingLimitSuccessContent(summary = state.data)
            is UiState.Error -> SpendingLimitErrorContent(onRetry = onRetry)
            is UiState.Idle -> SpendingLimitEmptyContent()
        }
    }
}