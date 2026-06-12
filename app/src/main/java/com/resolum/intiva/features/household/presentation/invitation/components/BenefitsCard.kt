package com.resolum.intiva.features.household.presentation.invitation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BenefitsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            BenefitItem(
                icon = Icons.Outlined.AccountBalanceWallet,
                title = "Gestión compartida",
                description = "Administra gastos y presupuestos del hogar en un solo lugar."
            )
            BenefitItem(
                icon = Icons.Outlined.TrackChanges,
                title = "Metas en conjunto",
                description = "Ahorra para objetivos familiares de manera colaborativa."
            )
            BenefitItem(
                icon = Icons.Outlined.Visibility,
                title = "Transparencia total",
                description = "Visualiza las transacciones y el progreso de todos los miembros."
            )
        }
    }
}
