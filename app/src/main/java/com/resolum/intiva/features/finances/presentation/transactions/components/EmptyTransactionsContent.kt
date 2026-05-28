package com.resolum.intiva.features.finances.presentation.transactions.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * This composable displays a message indicating that no transactions were found.
 * It is used when the transaction list is empty to inform the user.
 */
@Composable
fun EmptyTransactionsContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No se encontraron transacciones",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
    }
}