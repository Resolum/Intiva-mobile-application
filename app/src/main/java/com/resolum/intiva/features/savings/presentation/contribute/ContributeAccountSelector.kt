package com.resolum.intiva.features.savings.presentation.contribute

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.AccountBalanceWallet
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount.FinancialAccountViewModel
import java.text.NumberFormat
import java.util.Locale

private val BackgroundGray = Color(0xFFF5F5F5)
private val BrandPurple = Color(0xFF534AB7)

/**
 * Account selector for the contribute screen, reusing [FinancialAccountViewModel]
 * from the paymentmethodsandcategories feature (same pattern as [FinancialAccountSelector]).
 */
@Composable
fun ContributeAccountSelector(
  viewModel: FinancialAccountViewModel = hiltViewModel(),
  onAccountSelected: (FinancialAccount) -> Unit
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  var expanded by remember { mutableStateOf(false) }
  var selectedAccount by remember { mutableStateOf<FinancialAccount?>(null) }

  LaunchedEffect(Unit) {
    viewModel.getFinancialAccounts()
  }

  LaunchedEffect(uiState.accounts) {
    if (uiState.accounts.isNotEmpty() && selectedAccount == null) {
      selectedAccount = uiState.accounts.first()
      onAccountSelected(uiState.accounts.first())
    }
  }

  Column {
    Text(
      text = "ORIGEN DEL DINERO",
      color = IntivaColors.TextSecondary,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(10.dp))
    Box {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .border(1.dp, BackgroundGray, RoundedCornerShape(14.dp))
          .background(Color.White)
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .background(BackgroundGray, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              Icons.Outlined.AccountBalanceWallet,
              contentDescription = null,
              tint = BrandPurple,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = selectedAccount?.name ?: "Seleccionar cuenta",
              fontWeight = FontWeight.SemiBold,
              fontSize = 15.sp,
              color = IntivaColors.TextPrimary
            )
            selectedAccount?.let { account ->
              val formatted = NumberFormat.getCurrencyInstance(Locale("es", "PE"))
                .format(account.currentAmount)
              Text(
                text = "Disponible: $formatted",
                fontSize = 13.sp,
                color = IntivaColors.TextSecondary
              )
            }
          }
        }
        IconButton(onClick = { expanded = !expanded }) {
          Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
        }
      }
      DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        uiState.accounts.forEach { account ->
          DropdownMenuItem(
            text = { Text(account.name) },
            onClick = {
              selectedAccount = account
              onAccountSelected(account)
              expanded = false
            }
          )
        }
      }
    }
  }
}
