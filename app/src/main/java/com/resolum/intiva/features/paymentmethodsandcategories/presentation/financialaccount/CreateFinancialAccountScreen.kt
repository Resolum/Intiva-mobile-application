package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resolum.intiva.core.common.state.UiState

private val IntivaPrimary = Color(0xFF534AB7)
private val IntivaPrimaryDark = Color(0xFF3D329B)
private val IntivaSecondary = Color(0xFFCDEB45)
private val IntivaNeutral = Color(0xFF78767E)
private val IntivaBackground = Color(0xFFFFF8FF)
private val IntivaInputBackground = Color(0xFFF7F1FD)
private val IntivaBorder = Color(0xFFE8DFF0)
private val IntivaText = Color(0xFF22202A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFinancialAccountScreen(
    viewModel: FinancialAccountViewModel = hiltViewModel(),
    onAccountCreated: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var cardNumber by remember { mutableStateOf("") }
    var holderName by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var institution by remember { mutableStateOf("Bank of Family") }
    var currentAmount by remember { mutableStateOf("0") }
    var creditLimit by remember { mutableStateOf("") }
    var isMainCard by remember { mutableStateOf(true) }

    var expanded by remember { mutableStateOf(false) }

    val accountTypes = listOf(
        "Tarjeta de débito" to "debit_card",
        "Tarjeta de crédito" to "credit_card",
        "Billetera" to "wallet"
    )

    var selectedAccountTypeLabel by remember { mutableStateOf(accountTypes.first().first) }
    var selectedAccountTypeValue by remember { mutableStateOf(accountTypes.first().second) }

    LaunchedEffect(uiState.createAccountState) {
        if (uiState.createAccountState is UiState.Success) {
            viewModel.resetCreateAccountState()
            onAccountCreated()
        }
    }

    val isFormValid = holderName.isNotBlank() &&
            cardNumber.length >= 4 &&
            expirationDate.isNotBlank() &&
            cvv.isNotBlank()

    Scaffold(
        containerColor = IntivaBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Añadir Tarjeta",
                        color = IntivaText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Volver",
                            tint = IntivaText
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Outlined.HelpOutline,
                            contentDescription = "Ayuda",
                            tint = IntivaNeutral
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(IntivaBackground)
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            IntivaCardPreview(
                institution = institution.ifBlank { "Bank of Family" },
                holderName = holderName.ifBlank { "ALEJANDRO MARTÍNEZ" },
                cardNumber = cardNumber,
                expirationDate = expirationDate.ifBlank { "12/28" }
            )

            Spacer(modifier = Modifier.height(4.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                IntivaTextField(
                    value = selectedAccountTypeLabel,
                    onValueChange = {},
                    label = "Tipo de cuenta",
                    readOnly = true,
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

            Column {
                Text(
                    text = "Número de Tarjeta",
                    color = IntivaText,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                IntivaTextField(
                    value = cardNumber,
                    onValueChange = { value ->
                        cardNumber = value.filter { it.isDigit() }.take(16)
                    },
                    label = "0000 0000 0000 0000",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.CreditCard,
                            contentDescription = null,
                            tint = IntivaNeutral
                        )
                    },
                    keyboardType = KeyboardType.Number
                )
            }

            Column {
                Text(
                    text = "Nombre del Titular",
                    color = IntivaText,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                IntivaTextField(
                    value = holderName,
                    onValueChange = { holderName = it.uppercase() },
                    label = "Como aparece en la tarjeta",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = null,
                            tint = IntivaNeutral
                        )
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Fecha de Expiración",
                        color = IntivaText,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    IntivaTextField(
                        value = expirationDate,
                        onValueChange = { value ->
                            expirationDate = value.take(5)
                        },
                        label = "MM/YY",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = null,
                                tint = IntivaNeutral
                            )
                        },
                        keyboardType = KeyboardType.Number
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "CVV",
                        color = IntivaText,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    IntivaTextField(
                        value = cvv,
                        onValueChange = { value ->
                            cvv = value.filter { it.isDigit() }.take(4)
                        },
                        label = "123",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = IntivaNeutral
                            )
                        },
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            if (selectedAccountTypeValue == "credit_card") {
                IntivaTextField(
                    value = creditLimit,
                    onValueChange = { creditLimit = it },
                    label = "Límite de crédito",
                    keyboardType = KeyboardType.Decimal
                )
            }

            MainCardSwitch(
                checked = isMainCard,
                onCheckedChange = { isMainCard = it }
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    val visibleName = when {
                        holderName.isNotBlank() -> holderName
                        selectedAccountTypeValue == "wallet" -> "Billetera"
                        else -> selectedAccountTypeLabel
                    }

                    viewModel.createFinancialAccount(
                        name = visibleName,
                        accountType = selectedAccountTypeValue,
                        currencyCode = "PEN",
                        currentAmount = currentAmount.toDoubleOrNull() ?: 0.0,
                        institution = institution.ifBlank { null },
                        creditLimit = if (selectedAccountTypeValue == "credit_card") {
                            creditLimit.toDoubleOrNull()
                        } else {
                            null
                        }
                    )
                },
                enabled = isFormValid && uiState.createAccountState !is UiState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(14.dp),
                        spotColor = IntivaSecondary
                    ),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = IntivaSecondary,
                    contentColor = Color.Black,
                    disabledContainerColor = Color(0xFFE8E1EA),
                    disabledContentColor = IntivaNeutral
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (uiState.createAccountState is UiState.Loading) {
                        "Guardando..."
                    } else {
                        "Confirmar Tarjeta"
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun IntivaCardPreview(
    institution: String,
    holderName: String,
    cardNumber: String,
    expirationDate: String
) {
    val lastFour = cardNumber.takeLast(4).ifBlank { "4242" }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(132.dp)
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = IntivaPrimary
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            IntivaPrimary,
                            IntivaPrimaryDark
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "TARJETA INTIVA",
                    color = Color.White.copy(alpha = 0.55f),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = institution,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "•••• •••• •••• $lastFour",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "TITULAR",
                            color = Color.White.copy(alpha = 0.55f),
                            style = MaterialTheme.typography.labelSmall
                        )

                        Text(
                            text = holderName,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "EXPIRA",
                            color = Color.White.copy(alpha = 0.55f),
                            style = MaterialTheme.typography.labelSmall
                        )

                        Text(
                            text = expirationDate,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.align(Alignment.TopEnd),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.22f)
                )
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 36.dp, height = 26.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💳",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun IntivaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        placeholder = {
            Text(
                text = label,
                color = Color(0xFFC8C0D3)
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = IntivaInputBackground,
            unfocusedContainerColor = IntivaInputBackground,
            disabledContainerColor = IntivaInputBackground,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = IntivaText,
            unfocusedTextColor = IntivaText,
            cursorColor = IntivaPrimary
        )
    )
}

@Composable
private fun MainCardSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = IntivaBorder
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = RoundedCornerShape(50.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEDE8FF)
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.StarOutline,
                    contentDescription = null,
                    tint = IntivaPrimary,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Tarjeta Principal",
                    color = IntivaText,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Para pagos recurrentes y automáticos",
                    color = IntivaNeutral,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = IntivaPrimary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFD9D1E5)
                )
            )
        }
    }
}