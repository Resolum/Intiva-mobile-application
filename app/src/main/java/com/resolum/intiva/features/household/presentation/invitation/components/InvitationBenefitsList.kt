package com.resolum.intiva.features.household.presentation.invitation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InvitationBenefitsList(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "BENEFICIOS DEL GRUPO",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF4C3FF7)
            )

            BenefitItem(
                icon = Icons.Default.People,
                title = "Gestión Familiar",
                description = "Administra los gastos e ingresos de toda tu familia"
            )

            BenefitItem(
                icon = Icons.Default.AccountBalance,
                title = "Metas Compartidas",
                description = "Crea y sigue metas financieras en equipo"
            )

            BenefitItem(
                icon = Icons.Default.TrendingUp,
                title = "Control Total",
                description = "Visualiza el progreso financiero del grupo"
            )
        }
    }
}
