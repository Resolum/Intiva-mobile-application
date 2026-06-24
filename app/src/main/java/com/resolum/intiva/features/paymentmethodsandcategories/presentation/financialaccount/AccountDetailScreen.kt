package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.finances.domain.models.TransactionWithDesignResponse
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.outlined.Edit

private val HeroStart         = Color(0xFF534AB7)
private val HeroEnd           = Color(0xFF3B32A0)
private val IntivaBackground  = Color(0xFFF4F0FA)
private val IntivaNeutral     = Color(0xFF78767E)
private val IntivaTextPrimary = Color(0xFF1F1B2D)
private val IntivaAccent      = Color(0xFFCDEB45)
private val IntivaError       = Color(0xFFE53935)
private val IntivaIncome      = Color(0xFF2E7D32)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScreen(
    viewModel: AccountDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onEditClick: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.loadAccount() }

    Scaffold(
        containerColor = IntivaBackground,
        floatingActionButton = {
            uiState.account?.let { account ->
                FloatingActionButton(
                    onClick = { onEditClick(account.id) },
                    shape = CircleShape,
                    containerColor = IntivaAccent,
                    contentColor = Color.Black
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Editar cuenta",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = IntivaTextPrimary
                            )
                        }
                        Text(
                            text = "Volver a Cuentas",
                            fontSize = 14.sp,
                            color = IntivaNeutral
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        when (val state = uiState.accountState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = HeroStart) }
            }

            is UiState.Success -> {
                AccountDetailContent(
                    account = state.data,
                    transactions = uiState.recentTransactions,
                    transactionsLoading = uiState.transactionsState is UiState.Loading,
                    onDisableClick = { viewModel.disableAccount() },
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message ?: "Error al cargar la cuenta",
                        color = IntivaError,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            }

            UiState.Idle -> Unit
        }
    }
}

@Composable
private fun AccountDetailContent(
    account: FinancialAccount,
    transactions: List<TransactionWithDesignResponse>,
    transactionsLoading: Boolean,
    onDisableClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        HeroCard(account = account)

        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                SpendingLimitRow(account = account)
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFF0EDF7),
                    thickness = 1.dp
                )
                StatusRow(isActive = account.isActive, onDisableClick = onDisableClick)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Movimientos Recientes",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = IntivaTextPrimary
            )
            if (transactions.isNotEmpty()) {
                Text(
                    text = "Ver todos",
                    fontSize = 13.sp,
                    color = HeroStart,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            transactionsLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = HeroStart, modifier = Modifier.size(28.dp)) }
            }

            transactions.isEmpty() -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay movimientos para esta cuenta",
                            fontSize = 13.sp,
                            color = IntivaNeutral,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            else -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column {
                        transactions.forEachIndexed { index, tx ->
                            TransactionRow(transaction = tx)
                            if (index < transactions.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = Color(0xFFF0EDF7),
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
@Composable
private fun HeroCard(account: FinancialAccount) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(colors = listOf(HeroStart, HeroEnd)))
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = account.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = accountTypeLabel(account.accountType),
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(IntivaAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = heroIcon(account.accountType),
                        contentDescription = null,
                        tint = HeroStart,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "SALDO DISPONIBLE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = Color.White.copy(alpha = 0.75f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "S/ ${String.format("%,.2f", account.currentAmount)}",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = IntivaAccent
            )
        }
    }
}

@Composable
private fun SpendingLimitRow(account: FinancialAccount) {
    var limitEnabled by remember { mutableStateOf(account.creditLimit != null) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InfoIcon(Icons.Outlined.Tune)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Límite de Gastos",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = IntivaTextPrimary
            )
            Text(
                text = if (account.creditLimit != null)
                    "Activado: S/ ${String.format("%,.2f", account.creditLimit)} / mes"
                else
                    "Sin límite configurado",
                fontSize = 11.sp,
                color = IntivaNeutral
            )
        }
        Switch(
            checked = limitEnabled,
            onCheckedChange = { limitEnabled = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = HeroStart,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFD0CDE4)
            )
        )
    }
}

@Composable
private fun StatusRow(isActive: Boolean, onDisableClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InfoIcon(Icons.Outlined.Shield)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Estado",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = IntivaTextPrimary
            )
            Text(
                text = if (isActive) "Cuenta activa" else "Cuenta inactiva",
                fontSize = 11.sp,
                color = IntivaNeutral
            )
        }
        if (isActive) {
            Button(
                onClick = onDisableClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFEBEB),
                    contentColor = IntivaError
                ),
                elevation = null
            ) {
                Text(text = "Desactivar", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun TransactionRow(transaction: TransactionWithDesignResponse) {
    val isIncome = transaction.transactionType.lowercase() == "income"

    val iconBg = runCatching {
        Color(android.graphics.Color.parseColor(
            transaction.categoryDesign?.categoryColor ?: if (isIncome) "#E8F5E9" else "#EDE8FF"
        )).copy(alpha = 0.2f)
    }.getOrElse { if (isIncome) Color(0xFFE8F5E9) else Color(0xFFEDE8FF) }

    val iconTint = if (isIncome) IntivaIncome else HeroStart
    val txIcon   = if (isIncome) Icons.Outlined.ArrowDownward else Icons.Outlined.ArrowUpward

    val amountColor  = if (isIncome) IntivaIncome else IntivaTextPrimary
    val amountPrefix = if (isIncome) "+ S/" else "- S/"

    val amountValue = transaction.amount.toDoubleOrNull() ?: 0.0

    val dateDisplay = formatTransactionDate(transaction.registeredAt)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = txIcon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.description,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = IntivaTextPrimary
            )
            Text(
                text = dateDisplay,
                fontSize = 11.sp,
                color = IntivaNeutral
            )
        }
        Text(
            text = "$amountPrefix\n${String.format("%,.2f", amountValue)}",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = amountColor,
            textAlign = TextAlign.End,
            lineHeight = 18.sp
        )
    }
}


@Composable
private fun InfoIcon(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFFF0EDF7)),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = HeroStart, modifier = Modifier.size(18.dp))
    }
}

private fun accountTypeLabel(type: String): String = when (type.lowercase()) {
    "wallet", "billetera"                               -> "Cuenta Corriente"
    "debit", "debit_card", "tarjeta_debito"             -> "Tarjeta de Débito"
    "credit", "credit_card", "tarjeta_credito"          -> "Tarjeta de Crédito"
    else                                                -> type
}

private fun heroIcon(type: String): ImageVector = when (type.lowercase()) {
    "credit", "credit_card", "tarjeta_credito",
    "debit", "debit_card", "tarjeta_debito"             -> Icons.Outlined.CreditCard
    else                                                -> Icons.Outlined.AccountBalanceWallet
}

/**
 * Converts an ISO-8601 date string (e.g. "2025-03-12T20:00:00") to a human-readable label.
 */
private fun formatTransactionDate(raw: String): String {
    return try {
        val parts = raw.split("T")
        val dateParts = parts[0].split("-")     // ["2025","03","12"]
        val timeParts = parts.getOrNull(1)?.substring(0, 5) ?: "" // "20:00"
        val monthNames = listOf(
            "", "Ene", "Feb", "Mar", "Abr", "May", "Jun",
            "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
        )
        val month = monthNames.getOrElse(dateParts[1].toIntOrNull() ?: 0) { "" }
        "${dateParts[2]} $month, $timeParts"
    } catch (e: Exception) {
        raw
    }
}
