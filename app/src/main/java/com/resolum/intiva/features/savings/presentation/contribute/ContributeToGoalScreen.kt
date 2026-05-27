package com.resolum.intiva.features.savings.presentation.contribute

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount
import java.math.BigDecimal
import java.math.RoundingMode

private val BrandPurple = Color(0xFF534AB7)
private val AccentGreen = Color(0xFFCDEB45)
private val BackgroundGray = Color(0xFFF5F5F5)

/**
 * Screen 32 — "Aportar a Meta" (US-021).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContributeToGoalScreen(
    accountId: Long,
    goalId: Long,
    onNavigateBack: () -> Unit,
    onGoalCompleted: () -> Unit,
    onGoalUncompleted: () -> Unit,
    onContributionSuccess: (com.resolum.intiva.features.savings.domain.models.GoalContribution) -> Unit = {},
    viewModel: ContributeToGoalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedAccountId by remember { mutableLongStateOf(accountId) }

    LaunchedEffect(selectedAccountId, goalId) {
        viewModel.loadGoal(selectedAccountId, goalId)
    }

    val goal = uiState.goal
    val goalTitle = goal?.title ?: "Meta de ahorro"
    val currentAmount = goal?.currentAmount ?: BigDecimal.ZERO
    val targetAmount = goal?.targetAmount ?: BigDecimal.ONE
    val currencyCode = goal?.currencyCode ?: "PEN"

    val progress = remember(currentAmount, targetAmount) {
        if (targetAmount > BigDecimal.ZERO) {
            currentAmount.divide(targetAmount, 4, RoundingMode.HALF_UP).toFloat().coerceIn(0f, 1f)
        } else 0f
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = (uiState.goalState as? UiState.Error)?.message
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) snackbarHostState.showSnackbar(errorMessage)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BrandPurple
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            GoalProgressHeader(
                title = goalTitle,
                currentAmount = currentAmount,
                targetAmount = targetAmount,
                currencyCode = currencyCode,
                progress = progress,
                onNavigateBack = onNavigateBack
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    ContributeAccountSelector(
                        onAccountSelected = { account: FinancialAccount ->
                            selectedAccountId = account.id
                        }
                    )

                    HorizontalDivider(color = BackgroundGray)

                    AmountDisplay(
                        amountInput = uiState.amountInput,
                        currencyCode = currencyCode,
                        inputError = uiState.inputError
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    NumericKeypad(
                        onDigitClick = { viewModel.appendDigit(it) },
                        onDeleteClick = { viewModel.deleteLastDigit() }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val isLoading = uiState.goalState is UiState.Loading ||
                            uiState.isContributorIdLoading
                    val canContribute = uiState.contributorId != null &&
                            uiState.amountInput.isNotEmpty() &&
                            !isLoading
                    Button(
                        onClick = {
                            viewModel.contribute(
                                currencyCode = currencyCode,
                                onCompleted = onGoalCompleted,
                                onUncompleted = onGoalUncompleted,
                                onSuccess = onContributionSuccess
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentGreen,
                            disabledContainerColor = BackgroundGray
                        ),
                        enabled = canContribute
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = BrandPurple,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                text = "CONFIRMAR APORTE →",
                                color = Color(0xFF1A1A1A),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalProgressHeader(
    title: String,
    currentAmount: BigDecimal,
    targetAmount: BigDecimal,
    currencyCode: String,
    progress: Float,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.offset(x = (-8).dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = title, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$currencyCode ${currentAmount.toPlainString()}",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                color = AccentGreen,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$currencyCode ${targetAmount.toPlainString()}",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = AccentGreen,
            trackColor = Color.White.copy(alpha = 0.25f)
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun AmountDisplay(
    amountInput: String,
    currencyCode: String,
    inputError: String?
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Monto a aportar",
            color = IntivaColors.TextSecondary,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (amountInput.isEmpty()) "$currencyCode 0" else "$currencyCode $amountInput",
            color = IntivaColors.TextPrimary,
            fontSize = 46.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        if (inputError != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = inputError, color = IntivaColors.ErrorRed, fontSize = 13.sp)
        }
    }
}

@Composable
private fun NumericKeypad(
    onDigitClick: (String) -> Unit,
    onDeleteClick: () -> Unit
) {
    val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf(".", "0", "⌫")
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (row in rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (key in row) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (key == "⌫") onDeleteClick() else onDigitClick(key)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (key == "⌫") {
                            Icon(
                                Icons.Default.Backspace,
                                contentDescription = "Borrar",
                                tint = IntivaColors.TextPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = key,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Medium,
                                color = IntivaColors.TextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
