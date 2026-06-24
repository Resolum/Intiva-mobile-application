package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resolum.intiva.core.common.state.UiState

private val IntivaPrimary       = Color(0xFF3D3A8C)
private val IntivaBackground    = Color(0xFFF6F4FA)
private val IntivaNeutral       = Color(0xFF78767E)
private val IntivaTextPrimary   = Color(0xFF1F1B2D)

private data class AccountTypeOption(
    val label: String,
    val value: String,
    val icon: ImageVector
)

private val accountTypes = listOf(
    AccountTypeOption("Billetera", "wallet",      Icons.Outlined.AccountBalanceWallet),
    AccountTypeOption("Débito",    "debit_card",  Icons.Outlined.AccountBalance),
    AccountTypeOption("Crédito",   "credit_card", Icons.Outlined.CreditCard),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFinancialAccountScreen(
    viewModel: FinancialAccountViewModel = hiltViewModel(),
    onAccountCreated: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var name         by remember { mutableStateOf("") }
    var institution  by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(accountTypes.first()) }
    var amountRaw    by remember { mutableStateOf("") }   // digits only, no dot entry

    // Derived display: "0.00" when empty, otherwise insert decimal automatically
    val amountDisplay = amountRaw.toDoubleOrNull()?.let {
        String.format("%,.2f", it / 100.0)
    } ?: "0.00"

    val amount = amountRaw.toDoubleOrNull()?.div(100.0) ?: 0.0

    LaunchedEffect(uiState.createAccountState) {
        if (uiState.createAccountState is UiState.Success) {
            viewModel.resetCreateAccountState()
            onAccountCreated()
        }
    }

    val isFormValid = name.isNotBlank()

    Scaffold(
        containerColor = IntivaBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Agregar Cuenta",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = IntivaTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = IntivaTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding()
        ) {

            // ── Amount display ────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 24.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "SALDO INICIAL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = IntivaNeutral
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "S/.",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = IntivaTextPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = amountDisplay,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = IntivaTextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(IntivaPrimary)
                )
            }

            HorizontalDivider(color = Color(0xFFEEEAF5))

            // ── Name + type fields ────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = {
                        Text(
                            "Ej. Ahorros Familia",
                            color = Color(0xFFBBB7C8),
                            fontSize = 15.sp
                        )
                    },
                    label = { Text("Nombre de la cuenta", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color(0xFFF5F3F9),
                        focusedBorderColor = IntivaPrimary,
                        unfocusedBorderColor = Color.Transparent,
                        focusedLabelColor = IntivaNeutral,
                        unfocusedLabelColor = IntivaNeutral,
                        cursorColor = IntivaPrimary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Tipo de cuenta",
                    fontSize = 13.sp,
                    color = IntivaNeutral
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    accountTypes.forEach { type ->
                        TypeChip(
                            option = type,
                            isSelected = selectedType == type,
                            onClick = { selectedType = type },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Credit limit field (credit card only)
                if (selectedType.value == "credit_card") {
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = institution,
                        onValueChange = { institution = it },
                        placeholder = { Text("Institución (opcional)", color = Color(0xFFBBB7C8)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color(0xFFF5F3F9),
                            focusedBorderColor = IntivaPrimary,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = IntivaPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                val keys = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf(".", "0", "⌫")
                )
                keys.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { key ->
                            NumericKey(
                                label = key,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(60.dp),
                                onClick = {
                                    when (key) {
                                        "⌫" -> {
                                            if (amountRaw.isNotEmpty()) {
                                                amountRaw = amountRaw.dropLast(1)
                                            }
                                        }
                                        "." -> { /* handled via integer-cent approach */ }
                                        else -> {
                                            if (amountRaw.length < 10) {
                                                amountRaw += key
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.createFinancialAccount(
                            name = name,
                            accountType = selectedType.value,
                            currencyCode = "PEN",
                            currentAmount = amount,
                            institution = institution.ifBlank { null },
                            creditLimit = null
                        )
                    },
                    enabled = isFormValid && uiState.createAccountState !is UiState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IntivaPrimary,
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFCCC8DC)
                    )
                ) {
                    if (uiState.createAccountState is UiState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "CONFIRMAR CUENTA",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun TypeChip(
    option: AccountTypeOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg     = if (isSelected) Color(0xFFEDE8FF) else Color(0xFFF5F3F9)
    val border = if (isSelected) IntivaPrimary      else Color.Transparent
    val tint   = if (isSelected) IntivaPrimary      else IntivaNeutral

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.5.dp, border, RoundedCornerShape(12.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = option.icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = option.label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = tint,
            textAlign = TextAlign.Center
        )
    }
}

// ── Numeric key ───────────────────────────────────────────────────────────────

@Composable
private fun NumericKey(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (label == "⌫") {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Backspace,
                contentDescription = "Borrar",
                tint = Color(0xFF78767E),
                modifier = Modifier.size(22.dp)
            )
        } else {
            Text(
                text = label,
                fontSize = 24.sp,
                fontWeight = FontWeight.Light,
                color = IntivaTextPrimary
            )
        }
    }
}
