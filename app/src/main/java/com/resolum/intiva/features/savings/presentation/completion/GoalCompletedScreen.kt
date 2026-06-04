package com.resolum.intiva.features.savings.presentation.completion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.communications.presentation.notifications.InAppNotificationCard
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val BrandPurple = Color(0xFF3E33A3)
private val AccentGreen = Color(0xFFCDEB45)
private val CelebrationGreen = Color(0xFFCDEB45)
private val DarkGreenIcon = Color(0xFF5A7000)

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
        val scale by animateFloatAsState(
          targetValue = 1f,
          animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow)
        )

        Column(modifier = Modifier.fillMaxSize()) {
          state.notification?.let { notification ->
            AnimatedVisibility(
              visible = true,
              enter = slideInVertically() + fadeIn()
            ) {
              InAppNotificationCard(
                notification = notification,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
              )
            }
          }

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 40.dp),
            contentAlignment = Alignment.Center
          ) {
            Box(
              modifier = Modifier
                .size(160.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(CelebrationGreen),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = DarkGreenIcon,
                modifier = Modifier.size(80.dp)
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
      is GoalCompletionUiState.Uncompleted -> {
        val scale by animateFloatAsState(
          targetValue = 1f,
          animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow)
        )
        Column(modifier = Modifier.fillMaxSize()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 40.dp),
            contentAlignment = Alignment.Center
          ) {
            Box(
              modifier = Modifier
                .size(160.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(CelebrationGreen),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = DarkGreenIcon,
                modifier = Modifier.size(80.dp)
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

  val df = remember { DecimalFormat("#,##0", DecimalFormatSymbols(Locale.US)) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 24.dp, vertical = 32.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {

    Text(
      text = if (isCompleted) "¡Meta cumplida!" else "Meta no cumplida",
      fontSize = 32.sp,
      fontWeight = FontWeight.Bold,
      color = BrandPurple,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(8.dp))

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
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "RESUMEN DE LA META",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF888888),
          letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = goal.title,
          fontWeight = FontWeight.Bold,
          fontSize = 24.sp,
          color = IntivaColors.TextPrimary,
          textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.Bottom) {
          Text(
            text = "S/ ",
            fontSize = 20.sp,
            color = IntivaColors.TextSecondary,
            modifier = Modifier.padding(bottom = 4.dp)
          )
          Text(
            text = df.format(goal.targetAmount),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = BrandPurple
          )
        }

        Spacer(modifier = Modifier.height(24.dp))
        LinearProgressIndicator(
          progress = { progress },
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = if (isCompleted) AccentGreen else IntivaColors.TextSecondary,
          trackColor = Color(0xFFF0F0F0)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "0%",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = IntivaColors.TextSecondary
          )
          Text(
            text = if (isCompleted) "100% Alcanzado" else "${(progress * 100).toInt()}% Alcanzado",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreenIcon
          )
        }
      }
    }

    Spacer(modifier = Modifier.weight(1f, fill = true))
    Spacer(modifier = Modifier.height(32.dp))

    Button(
      onClick = onPrimaryClick,
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
    ) {
      Text(primaryLabel, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedButton(
      onClick = onSecondaryClick,
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPurple),
      border = BorderStroke(1.dp, Color(0xFFD0D0D0))
    ) {
      Text(secondaryLabel, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BrandPurple)
    }
  }
}

