package com.resolum.intiva.features.savings.presentation.completion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import java.math.BigDecimal
import java.math.RoundingMode

private val BrandPurple = Color(0xFF534AB7)
private val AccentGreen = Color(0xFFCDEB45)
private val CelebrationGreen = Color(0xFFE8F5C8)

/**
 * Screen 33 — "Meta Cumplida" celebration layout (US-022).
 */
@Composable
fun GoalCompletedScreen(
  accountId: Long,
  goalId: Long,
  onNewGoal: () -> Unit,
  onBack: () -> Unit,
  viewModel: GoalCompletionViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsState()

  LaunchedEffect(accountId, goalId) {
    viewModel.loadGoal(accountId, goalId)
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(IntivaColors.BackgroundSurface)
  ) {
    when (val state = uiState) {
      is GoalCompletionUiState.Loading -> {
        CircularProgressIndicator(
          modifier = Modifier.align(Alignment.Center),
          color = BrandPurple
        )
      }
      is GoalCompletionUiState.Error -> {
        Text(
          text = state.message,
          modifier = Modifier
            .align(Alignment.Center)
            .padding(24.dp),
          textAlign = TextAlign.Center,
          color = IntivaColors.TextSecondary
        )
      }
      is GoalCompletionUiState.Completed -> {
        Column(modifier = Modifier.fillMaxSize()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 48.dp),
            contentAlignment = Alignment.Center
          ) {
            Box(
              modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(CelebrationGreen),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFF5A7000),
                modifier = Modifier.size(56.dp)
              )
            }
          }
          GoalOutcomeLayout(
            goal = state.goal,
            isCompleted = true,
            onPrimaryClick = onNewGoal,
            onSecondaryClick = onBack,
            primaryLabel = "Nueva Meta",
            secondaryLabel = "Volver"
          )
        }
      }
      is GoalCompletionUiState.Uncompleted -> Unit
    }
  }
}

@Composable
internal fun GoalOutcomeLayout(
  goal: SavingGoal,
  isCompleted: Boolean,
  onPrimaryClick: () -> Unit,
  onSecondaryClick: () -> Unit,
  primaryLabel: String,
  secondaryLabel: String
) {
  val progress = if (goal.targetAmount > BigDecimal.ZERO) {
    (goal.currentAmount.divide(goal.targetAmount, 4, RoundingMode.HALF_UP))
      .toFloat()
      .coerceIn(0f, 1f)
  } else 0f

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 24.dp, vertical = 48.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = if (isCompleted) "¡Meta cumplida!" else "Meta no cumplida",
      fontSize = 28.sp,
      fontWeight = FontWeight.Bold,
      color = IntivaColors.TextPrimary,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = if (isCompleted) {
        "Has completado exitosamente tu objetivo."
      } else {
        "No lograste alcanzar tu objetivo esta vez."
      },
      fontSize = 16.sp,
      color = IntivaColors.TextSecondary,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(32.dp))

    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Column(modifier = Modifier.padding(24.dp)) {
        Text(
          text = goal.title,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = IntivaColors.TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "${goal.currencyCode} ${goal.currentAmount.toPlainString()} / ${goal.targetAmount.toPlainString()}",
          fontSize = 15.sp,
          color = IntivaColors.TextSecondary
        )
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
          progress = { progress },
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = if (isCompleted) AccentGreen else IntivaColors.TextSecondary,
          trackColor = IntivaColors.BackgroundSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "${(progress * 100).toInt()}%",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = BrandPurple
        )
      }
    }

    Spacer(modifier = Modifier.height(40.dp))

    Button(
      onClick = onPrimaryClick,
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
      shape = RoundedCornerShape(30.dp),
      colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
    ) {
      Text(primaryLabel, color = Color.White, fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedButton(
      onClick = onSecondaryClick,
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
      shape = RoundedCornerShape(30.dp),
      colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPurple)
    ) {
      Text(secondaryLabel, fontWeight = FontWeight.Bold)
    }
  }
}
