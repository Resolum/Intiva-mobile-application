package com.resolum.intiva.features.savings.presentation.completion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.resolum.intiva.core.ui.theme.IntivaColors

/**
 * "Meta No Cumplida" outcome screen (US-022), following the Intiva design system.
 */
@Composable
fun GoalUncompletedScreen(
  accountId: Long,
  goalId: Long,
  onTryAgain: () -> Unit,
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
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
      is GoalCompletionUiState.Uncompleted -> {
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
                .background(Color(0xFFE5E5E5)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                Icons.Default.Close,
                contentDescription = null,
                tint = IntivaColors.TextSecondary,
                modifier = Modifier.size(56.dp)
              )
            }
          }
          GoalOutcomeLayout(
            goal = state.goal,
            isCompleted = false,
            onPrimaryClick = onTryAgain,
            onSecondaryClick = onBack,
            primaryLabel = "Intentar de nuevo",
            secondaryLabel = "Volver"
          )
        }
      }
      is GoalCompletionUiState.Completed -> Unit
    }
  }
}
