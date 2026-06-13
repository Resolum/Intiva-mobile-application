package com.resolum.intiva.features.finances.presentation.transactions

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.finances.domain.models.Transaction
import com.resolum.intiva.features.finances.domain.models.TransactionType
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    transactionId: Long,
    onNavigateBack: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(transactionId) {
        viewModel.getTransactionById(transactionId)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Resumen",
                        color = IntivaColors.TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF4F4A58)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            when (val state = uiState.transactionDetailState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = IntivaColors.PrimaryBrand
                    )
                }

                is UiState.Success -> {
                    TransactionDetailContent(
                        transaction = state.data,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is UiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun TransactionDetailContent(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    val isIncome = transaction.transactionType == TransactionType.INCOME.name
    val amountPrefix = if (isIncome) "+" else "-"
    val amountColor = if (isIncome) Color(0xFF42A94F) else IntivaColors.TextPrimary

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(Color(0xFFEFE9F7)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Storefront,
                contentDescription = null,
                tint = IntivaColors.PrimaryBrand,
                modifier = Modifier.size(34.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = transaction.currencyCode,
                color = Color(0xFF817A8D),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = " $amountPrefix${transaction.amount.formatAmount()}",
                color = amountColor,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(38.dp))

        DetailCard(transaction = transaction)

        Spacer(modifier = Modifier.weight(1f))

        ShareButton()

        TextButton(
            onClick = { /* TODO */ },
            modifier = Modifier.padding(top = 18.dp, bottom = 22.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ReportProblem,
                contentDescription = null,
                tint = IntivaColors.PrimaryBrand,
                modifier = Modifier.size(19.dp)
            )
            Text(
                text = " Reportar problema",
                color = IntivaColors.PrimaryBrand,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DetailCard(transaction: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            val detailDateTime = transaction.registeredAt?.toDetailDateTime()
            DetailRow(
                label = "Descripcion",
                value = transaction.description.ifBlank { "Transaccion #${transaction.id}" }
            )
            DetailRow(
                label = "Fecha",
                value = detailDateTime?.first ?: "No disponible"
            )
            DetailRow(
                label = "Hora",
                value = detailDateTime?.second ?: "No disponible"
            )
            DetailRow(
                label = "Cuenta origen",
                value = transaction.financialAccountName
                    ?: transaction.financialAccount?.name
                    ?: "Cuenta #${transaction.financialAccountId}",
                supportingValue = transaction.financialAccount?.institution
                    ?: transaction.financialAccount?.accountType
            )
            DetailRow(
                label = "Estado",
                value = "Completado",
                chip = true,
                showDivider = false
            )
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    supportingValue: String? = null,
    chip: Boolean = false,
    showDivider: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Color(0xFF565160),
                fontSize = 17.sp
            )

            if (chip) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFE8E1FF))
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircleOutline,
                        contentDescription = null,
                        tint = IntivaColors.PrimaryBrand,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = " $value",
                        color = IntivaColors.PrimaryBrand,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            } else {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = value,
                        color = IntivaColors.TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End
                    )
                    supportingValue?.let {
                        Text(
                            text = it,
                            color = Color(0xFF817A8D),
                            fontSize = 13.sp,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }

        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFE9E4EF))
            )
        }
    }
}

@Composable
private fun ShareButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(IntivaColors.PrimaryAction),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = Color(0xFF6E7A13)
            )
            Text(
                text = "  COMPARTIR",
                color = Color(0xFF6E7A13),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        }
    }
}

private fun String.formatAmount(): String {
    return runCatching {
        BigDecimal(this).setScale(2).toPlainString()
    }.getOrElse { this }
}

private fun String.toDetailDateTime(): Pair<String, String>? {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.forLanguageTag("es-PE"))
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.forLanguageTag("es-PE"))
    return runCatching {
        val dateTime = Instant.parse(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
        dateTime.toFormattedPair(dateFormatter, timeFormatter)
    }.recoverCatching {
        val dateTime = OffsetDateTime.parse(this).atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        dateTime.toFormattedPair(dateFormatter, timeFormatter)
    }.recoverCatching {
        LocalDateTime.parse(this).toFormattedPair(dateFormatter, timeFormatter)
    }.getOrNull()
}

private fun LocalDateTime.toFormattedPair(
    dateFormatter: DateTimeFormatter,
    timeFormatter: DateTimeFormatter
): Pair<String, String> {
    val date = toLocalDate().format(dateFormatter)
    val time = toLocalTime().format(timeFormatter)
        .replace("a. m.", "AM")
        .replace("p. m.", "PM")
    return date to time
}
