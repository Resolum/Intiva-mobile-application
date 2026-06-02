package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resolum.intiva.core.common.state.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFinancialAccountScreen(
    viewModel: FinancialAccountViewModel = hiltViewModel(),
    onAccountCreated: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var currentAmount by remember { mutableStateOf("") }
    var institution by remember { mutableStateOf("") }
    var creditLimit by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    val accountTypes = listOf(
        "Tarjeta de débito" to "debit_card",
        "Tarjeta de crédito" to "credit_card",
        "Billetera" to "wallet"
    )

    var selectedAccountTypeLabel by remember { mutableStateOf(accountTypes.first().first) }
    var selectedAccountTypeValue by remember { mutableStateOf(accountTypes.first().second) }

    val currencyCode = "PEN"

    LaunchedEffect(uiState.createAccountState) {
        if (uiState.createAccountState is UiState.Success) {
            viewModel.resetCreateAccountState()
            onAccountCreated()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Registrar cuenta financiera",
            style = MaterialTheme.typography.headlineSmall
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedAccountTypeLabel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de cuenta") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                accountTypes.forEach { accountType ->
                    DropdownMenuItem(
                        text = { Text(accountType.first) },
                        onClick = {
                            selectedAccountTypeLabel = accountType.first
                            selectedAccountTypeValue = accountType.second
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            placeholder = { Text("Ej: BCP Débito, Interbank Crédito") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = currentAmount,
            onValueChange = { currentAmount = it },
            label = { Text("Saldo actual") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = institution,
            onValueChange = { institution = it },
            label = { Text("Institución") },
            placeholder = { Text("Ej: BCP, BBVA, Yape") },
            modifier = Modifier.fillMaxWidth()
        )

        if (selectedAccountTypeValue == "credit_card") {
            OutlinedTextField(
                value = creditLimit,
                onValueChange = { creditLimit = it },
                label = { Text("Límite de crédito") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (uiState.createAccountState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Error -> {
                Text(
                    text = "No se pudo registrar la cuenta.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> Unit
        }

        Button(
            onClick = {
                val amount = currentAmount.toDoubleOrNull() ?: 0.0
                val limit = if (selectedAccountTypeValue == "credit_card") {
                    creditLimit.toDoubleOrNull()
                } else {
                    null
                }

                viewModel.createFinancialAccount(
                    name = name,
                    accountType = selectedAccountTypeValue,
                    currencyCode = currencyCode,
                    currentAmount = amount,
                    institution = institution.ifBlank { null },
                    creditLimit = limit
                )
            },
            enabled = name.isNotBlank() && currentAmount.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrar")
        }
    }
}