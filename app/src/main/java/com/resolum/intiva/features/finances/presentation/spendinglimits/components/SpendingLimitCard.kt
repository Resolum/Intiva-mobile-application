package com.resolum.intiva.features.finances.presentation.spendinglimits.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.finances.presentation.spendinglimits.SpendingLimitSummary
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category

@Composable
fun SpendingLimitCard(
    state: UiState<SpendingLimitSummary>,
    category: Category? = null,
    onRetry: () -> Unit,
    onOpenAlert: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Card(
        modifier = modifier
            .clickable { onOpenAlert() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(
            width = 1.dp,
            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE5E7EF))
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        when (state) {
            is UiState.Loading -> SpendingLimitLoadingContent()
            is UiState.Success -> SpendingLimitSuccessContent(summary = state.data, category = category)
            is UiState.Error -> SpendingLimitErrorContent(onRetry = onRetry)
            is UiState.Idle -> SpendingLimitEmptyContent()
        }
    }
}
