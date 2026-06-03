package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount

private val IntivaPrimary = Color(0xFF534AB7)
private val IntivaSecondary = Color(0xFFCDEB45)
private val IntivaNeutral = Color(0xFF78767E)
private val IntivaCardBackground = Color(0xFFF8F5FF)

@Composable
fun FinancialAccountCard(
    account: FinancialAccount,
    modifier: Modifier = Modifier
) {
    val icon = when (account.accountType.lowercase()) {
        "credit", "credit_card", "tarjeta_credito" -> Icons.Outlined.CreditCard
        "debit", "debit_card", "tarjeta_debito" -> Icons.Outlined.AccountBalance
        "wallet", "billetera" -> Icons.Outlined.AccountBalanceWallet
        else -> Icons.Outlined.AccountBalanceWallet
    }

    val accountTypeText = when (account.accountType.lowercase()) {
        "credit", "credit_card", "tarjeta_credito" -> "Tarjeta de crédito"
        "debit", "debit_card", "tarjeta_debito" -> "Tarjeta de débito"
        "wallet", "billetera" -> "Billetera digital"
        else -> account.accountType
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = IntivaCardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = RoundedCornerShape(50.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = IntivaPrimary
                    )
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .padding(12.dp)
                            .size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = account.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )

                    Text(
                        text = accountTypeText,
                        style = MaterialTheme.typography.bodySmall,
                        color = IntivaNeutral
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "${account.currencyCode} ${account.currentAmount}",
                style = MaterialTheme.typography.headlineSmall,
                color = IntivaPrimary
            )

            account.institution?.let { institution ->
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = institution,
                    style = MaterialTheme.typography.bodyMedium,
                    color = IntivaNeutral
                )
            }

            account.creditLimit?.let { limit ->
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Límite: ${account.currencyCode} $limit",
                    style = MaterialTheme.typography.bodySmall,
                    color = IntivaNeutral
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (account.isActive) "Activa" else "Inactiva",
                style = MaterialTheme.typography.labelMedium,
                color = if (account.isActive) IntivaPrimary else IntivaNeutral
            )
        }
    }
}