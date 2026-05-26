package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount.FinancialAccountViewModel

/**
 * Composable function that displays a dropdown menu for selecting a financial account.
 *
 * This component fetches the list of financial accounts from the FinancialAccountViewModel and allows the user
 * to select one. The selected account is displayed in the dropdown button, and when an account is selected,
 * the onAccountSelected callback is invoked with the selected account.
 *
 * @param viewModel The ViewModel responsible for managing the state of financial accounts.
 * @param onAccountSelected Callback function that is called when a financial account is selected, providing the selected account as a parameter.
 */
@Composable
fun FinancialAccountSelector(
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

    Box {
        Surface(
            onClick = { expanded = !expanded },
            shape = RoundedCornerShape(50.dp),
            color = Color.White.copy(alpha = 0.2f),
            modifier = Modifier.height(44.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountBalanceWallet,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    text = selectedAccount?.name ?: "Seleccionar cuenta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )

                Icon(
                    imageVector = if (expanded)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            uiState.accounts.forEach { account ->
                DropdownMenuItem(
                    text = { Text(text = account.name) },
                    onClick = {
                        selectedAccount = account
                        onAccountSelected(account)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.AccountBalanceWallet,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}