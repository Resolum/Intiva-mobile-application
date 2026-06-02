package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount

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

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = account.name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = account.accountType,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${account.currencyCode} ${account.currentAmount}",
                style = MaterialTheme.typography.titleLarge
            )

            account.institution?.let { institution ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = institution,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            account.creditLimit?.let { limit ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Límite: ${account.currencyCode} $limit",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}