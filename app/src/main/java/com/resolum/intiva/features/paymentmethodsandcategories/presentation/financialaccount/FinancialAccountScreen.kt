package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount.components.DisableAccountDialog

private val IntivaPrimary      = Color(0xFF534AB7)
private val IntivaSecondary    = Color(0xFFCDEB45)
private val IntivaNeutral      = Color(0xFF78767E)
private val IntivaBackground   = Color(0xFFF4F0FA)
private val IntivaTextPrimary  = Color(0xFF1F1B2D)

@Composable
fun FinancialAccountScreen(
    viewModel: FinancialAccountViewModel = hiltViewModel(),
    onAddAccountClick: () -> Unit = {},
    onAccountClick: ((Long) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getFinancialAccounts()
    }

    if (uiState.showDisableConfirmDialog && uiState.accountToDisable != null) {
        DisableAccountDialog(
            account = uiState.accountToDisable!!,
            onConfirm = { viewModel.confirmDisableAccount() },
            onDismiss = { viewModel.onDismissDisableDialog() }
        )
    }

    Scaffold(
        containerColor = IntivaBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddAccountClick,
                shape = CircleShape,
                containerColor = IntivaSecondary,
                contentColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Agregar cuenta",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(IntivaBackground)
                .padding(paddingValues)
        ) {
            // ── Header ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Cuentas",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = IntivaTextPrimary
                    )
                    Text(
                        text = "Gestiona tus balances y\ntarjetas",
                        fontSize = 13.sp,
                        color = IntivaNeutral,
                        lineHeight = 18.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "BALANCE TOTAL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = IntivaNeutral,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "S/ ${String.format("%,.2f", viewModel.totalBalance)}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = IntivaPrimary
                    )
                }
            }

            // ── Content ─────────────────────────────────────────────────────
            when (uiState.accountsState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = IntivaPrimary)
                    }
                }

                is UiState.Success -> {
                    if (uiState.accounts.isEmpty()) {
                        EmptyAccountsContent(onAddAccountClick = onAddAccountClick)
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.accounts) { account ->
                                AccountListCard(
                                    account = account,
                                    onClick = { onAccountClick?.invoke(account.id) }
                                )
                            }

                            item {
                                Text(
                                    text = "Mantén un registro claro de tus finanzas\nconectando todas tus cuentas en un solo lugar.",
                                    fontSize = 13.sp,
                                    color = IntivaNeutral,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 18.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp, bottom = 16.dp)
                                )
                            }
                        }
                    }
                }

                is UiState.Error -> {
                    EmptyAccountsContent(onAddAccountClick = onAddAccountClick)
                }

                UiState.Idle -> Unit
            }
        }
    }
}


@Composable
private fun AccountListCard(
    account: FinancialAccount,
    onClick: () -> Unit
) {
    val (icon, iconBg, iconTint, badge) = accountVisuals(account.accountType)

    val amountColor = if (account.accountType.lowercase().contains("credit")) {
        Color(0xFFE53935)
    } else {
        IntivaTextPrimary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = account.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = IntivaTextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                badge?.let { badgeText ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF8BC34A))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = badgeText,
                            fontSize = 11.sp,
                            color = IntivaNeutral
                        )
                    }
                } ?: run {
                    val subtitle = accountSubtitle(account)
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = IntivaNeutral
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "S/ ${String.format("%,.2f", account.currentAmount)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = IntivaTextPrimary
                )
                if (account.accountType.lowercase().contains("credit")) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Deuda",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE53935)
                    )
                }
            }
        }
    }
}


private data class AccountVisuals(
    val icon: ImageVector,
    val iconBackground: Color,
    val iconTint: Color,
    val badge: String?
)

private fun accountVisuals(type: String): AccountVisuals = when (type.lowercase()) {
    "wallet", "billetera" -> AccountVisuals(
        icon = Icons.Outlined.AccountBalanceWallet,
        iconBackground = Color(0xFFEDE8FF),
        iconTint = Color(0xFF534AB7),
        badge = "Principal"
    )
    "debit", "debit_card", "tarjeta_debito" -> AccountVisuals(
        icon = Icons.Outlined.AccountBalance,
        iconBackground = Color(0xFFE3F2FD),
        iconTint = Color(0xFF1565C0),
        badge = null
    )
    "credit", "credit_card", "tarjeta_credito" -> AccountVisuals(
        icon = Icons.Outlined.CreditCard,
        iconBackground = Color(0xFFE8F5E9),
        iconTint = Color(0xFF2E7D32),
        badge = null
    )
    else -> AccountVisuals(
        icon = Icons.Outlined.AccountBalanceWallet,
        iconBackground = Color(0xFFEDE8FF),
        iconTint = IntivaPrimary,
        badge = null
    )
}

private fun accountSubtitle(account: FinancialAccount): String {
    return when {
        account.institution != null -> account.institution
        account.creditLimit != null -> "Cierre: 15 de Oct"
        else -> ""
    }
}

@Composable
private fun EmptyAccountsContent(onAddAccountClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEDE8FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountBalanceWallet,
                    contentDescription = null,
                    tint = IntivaPrimary,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Sin cuentas registradas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = IntivaTextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Toca el botón + para agregar\ntu primera cuenta.",
                fontSize = 13.sp,
                color = IntivaNeutral,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}
