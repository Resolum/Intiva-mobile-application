package com.resolum.intiva.features.savings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.savings.domain.models.GoalContribution
import com.resolum.intiva.features.savings.domain.models.SavingGoalStatus
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Screen 31 — Detalle de Meta (US-021 / US-022).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsGoalDetailScreen(
    accountId: Long,
    goalId: Long,
    onNavigateBack: () -> Unit,
    onContributeClick: () -> Unit = {},
    onGoalCompleted: () -> Unit = {},
    onGoalUncompleted: () -> Unit = {},
    viewModel: SavingsGoalDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(accountId, goalId) {
        viewModel.loadGoal(accountId, goalId)
    }

    // Refresh goal data when returning from the contribute screen
    LifecycleResumeEffect(accountId, goalId) {
        viewModel.refreshGoal(accountId, goalId)
        onPauseOrDispose { }
    }

    val goal = uiState.goal
    LaunchedEffect(goal?.status) {
        when (goal?.let { SavingGoalStatus.from(it.status) }) {
            SavingGoalStatus.COMPLETED -> onGoalCompleted()
            SavingGoalStatus.UNCOMPLETED -> onGoalUncompleted()
            else -> Unit
        }
    }

    val progress = remember(goal) {
        val g = goal ?: return@remember 0f
        if (g.targetAmount > BigDecimal.ZERO) {
            g.currentAmount.divide(g.targetAmount, 4, RoundingMode.HALF_UP).toFloat().coerceIn(0f, 1f)
        } else 0f
    }

    val isLoading = uiState.goalState is UiState.Loading

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = goal?.title ?: "Meta de ahorro",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = IntivaColors.TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = IntivaColors.TextPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IntivaColors.BackgroundSurface)
            )
        },
        bottomBar = {
            if (goal != null && SavingGoalStatus.from(goal.status) == SavingGoalStatus.INPROGRESS) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(IntivaColors.BackgroundSurface)
                        .padding(20.dp)
                ) {
                    Button(
                        onClick = onContributeClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IntivaColors.PrimaryAction),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = IntivaColors.TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Aportar",
                            color = IntivaColors.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        },
        containerColor = IntivaColors.BackgroundSurface
    ) { padding ->
        if (isLoading && goal == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = IntivaColors.PrimaryBrand)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(220.dp)) {
                        CircularProgressIndicator(
                            progress = { 1f },
                            modifier = Modifier.fillMaxSize(),
                            color = IntivaColors.BackgroundSurface,
                            strokeWidth = 14.dp
                        )
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxSize(),
                            color = IntivaColors.PrimaryAction,
                            strokeWidth = 14.dp,
                            trackColor = Color.Transparent
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${(progress * 100).toInt()}%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 42.sp,
                                color = IntivaColors.PrimaryBrand
                            )
                            goal?.let { g ->
                                Text(
                                    text = "${g.currencyCode} ${g.currentAmount.toPlainString()}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = IntivaColors.TextPrimary
                                )
                                Text(
                                    text = "DE ${g.currencyCode} ${g.targetAmount.toPlainString()}",
                                    fontSize = 14.sp,
                                    color = IntivaColors.TextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "PROGRESO",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = IntivaColors.TextSecondary
                            )
                            Text(
                                text = if (progress >= 0.5f) "Excelente ritmo" else "Sigue aportando",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = IntivaColors.PrimaryBrand
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFF4FAD8), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = Color(0xFF5A7000),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = IntivaColors.PrimaryBrand,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TIEMPO",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = IntivaColors.PrimaryBrand
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = goal?.deadline?.let { formatDaysRemaining(it) } ?: "—",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = IntivaColors.TextPrimary
                        )
                        Text(text = "Fecha límite", fontSize = 14.sp, color = IntivaColors.TextSecondary)
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Familiares contribuyendo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = IntivaColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        repeat(3) { index ->
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .offset(x = (-12 * index).dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (index) {
                                            0 -> Color.Gray
                                            1 -> Color.DarkGray
                                            else -> IntivaColors.PrimaryBrand
                                        }
                                    )
                                    .border(3.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .offset(x = (-36).dp)
                                .background(Color.White, CircleShape)
                                .border(2.dp, Color.LightGray, CircleShape)
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Añadir", tint = Color.Gray)
                        }
                    }
                }
            }

            Text(
                text = "Historial de aportes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = IntivaColors.TextPrimary,
                modifier = Modifier.padding(top = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column {
                    if (uiState.contributions.isEmpty()) {
                        Text(
                            text = "Aún no hay aportes registrados.",
                            modifier = Modifier.padding(20.dp),
                            color = IntivaColors.TextSecondary
                        )
                    } else {
                        uiState.contributions.forEachIndexed { index, contribution ->
                            if (index > 0) {
                                HorizontalDivider(
                                    color = IntivaColors.BackgroundSurface,
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                )
                            }
                            ContributionHistoryItem(contribution = contribution)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun ContributionHistoryItem(contribution: GoalContribution) {
    HistoryItem(
        name = "Contribuidor #${contribution.contributorId}",
        time = formatContributionDate(contribution.contributedAt),
        amount = "+ ${contribution.amountContributed.currencyCode} ${contribution.amountContributed.amount.toPlainString()}",
        iconBg = IntivaColors.PrimaryBrand,
        isAuto = false
    )
}

@Composable
fun HistoryItem(
    name: String,
    time: String,
    amount: String,
    iconBg: Color,
    isAuto: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(iconBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isAuto) {
                Icon(Icons.Default.Sync, contentDescription = null, tint = IntivaColors.PrimaryBrand)
            } else {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = IntivaColors.TextPrimary)
            Text(text = time, fontSize = 14.sp, color = IntivaColors.TextSecondary)
        }
        Text(text = amount, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF5A7000))
    }
}

private fun formatContributionDate(isoDate: String): String {
    return try {
        val dateTime = LocalDateTime.parse(isoDate.take(19))
        dateTime.format(DateTimeFormatter.ofPattern("dd MMM, hh:mm a"))
    } catch (_: DateTimeParseException) {
        isoDate
    }
}

private fun formatDaysRemaining(deadline: String): String {
    return try {
        val date = java.time.LocalDate.parse(deadline.take(10))
        val days = java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), date)
        if (days >= 0) "$days días" else "Vencida"
    } catch (_: Exception) {
        deadline.take(10)
    }
}
