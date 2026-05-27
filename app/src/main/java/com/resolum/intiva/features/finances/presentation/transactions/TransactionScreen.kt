package com.resolum.intiva.features.finances.presentation.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.resolum.intiva.core.ui.components.IntivaBackButton
import com.resolum.intiva.core.ui.snackbar.IntivaSnackBarHost
import com.resolum.intiva.core.ui.snackbar.SnackBarBus
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.finances.domain.models.TransactionType
import com.resolum.intiva.features.finances.presentation.transactions.components.AmountInput.appendDigit
import com.resolum.intiva.features.finances.presentation.transactions.components.AmountInput.deleteDigit
import com.resolum.intiva.features.finances.presentation.transactions.components.NumPad
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.components.CategoryGrid
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount.components.FinancialAccountSelector
import kotlinx.coroutines.flow.collectLatest

/**
 * Composable function representing the transaction form screen for registering a new financial transaction.
 *
 * This screen allows users to input the transaction amount, select a category and financial account, and save the transaction.
 *
 * @param transactionType The type of transaction being registered (e.g., income, expense).
 * @param onDismiss A callback function to be invoked when the user wants to dismiss the screen.
 * @param viewModel The ViewModel managing the state and logic for this screen, provided by Hilt.
 */
@Composable
fun TransactionFormScreen(
    transactionType: TransactionType,
    onDismiss: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel(),
) {
    var amountText by remember { mutableStateOf("0.00") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedAccount by remember { mutableStateOf<FinancialAccount?>(null) }
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }


    LaunchedEffect(Unit) {
        SnackBarBus.messages.collectLatest { event ->
            snackBarHostState.showSnackbar(event)
        }
    }

    if (uiState.navigateBack) {
        LaunchedEffect(Unit) {
            onDismiss()
        }
    }

    val title = when (transactionType) {
        TransactionType.INCOME -> "Nuevo Ingreso"
        TransactionType.EXPENSE -> "Nuevo Gasto"
        else -> "Nueva Transacción"
    }

    val saveLabel = when (transactionType) {
        TransactionType.INCOME -> "Registrar Ingreso"
        TransactionType.EXPENSE -> "Registrar Gasto"
        else -> "Guardar"
    }

    val headerBackground = when (transactionType) {
        TransactionType.INCOME -> IntivaColors.BackgroundPurple
        TransactionType.EXPENSE -> IntivaColors.BackgroundPurple
        else -> IntivaColors.BackgroundPurple
    }

    Scaffold(
        snackbarHost = {
            IntivaSnackBarHost(hostState = snackBarHostState)
        }
    ) {
        padding -> Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBackground)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                IntivaBackButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBackground)
                    .padding(bottom = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "S/. $amountText",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FinancialAccountSelector(
                        onAccountSelected = { selectedAccount = it }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                CategoryGrid(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                NumPad(
                    onNumberClick = { digit ->
                        amountText = appendDigit(amountText, digit)
                    },
                    onDecimalClick = {
                        if (!amountText.contains("."))
                            amountText = "$amountText."
                    },
                    onDeleteClick = {
                        amountText = deleteDigit(amountText)
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.registerIndividualTransaction(
                            amount = amountText,
                            category = selectedCategory,
                            account = selectedAccount,
                            transactionType = transactionType
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFCCFF00)
                    )
                ) {
                    Text(
                        text = saveLabel,
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}