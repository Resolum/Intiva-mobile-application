package com.resolum.intiva.features.finances.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.resolum.intiva.core.ui.components.IntivaBackButton
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.finances.domain.model.TransactionType
import com.resolum.intiva.features.finances.presentation.components.NumPad
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.components.CategoryGrid
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount.components.FinancialAccountSelector

@Composable
fun TransactionFormScreen(
    transactionType: TransactionType,
    onDismiss: () -> Unit,
    onSave: (amount: Double, categoryId: Long, accountId: Long) -> Unit
) {
    var amountText by remember { mutableStateOf("0.00") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedAccount by remember { mutableStateOf<FinancialAccount?>(null) }

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

    Column(
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
                    // TODO: Validar campos y llamar a onSave con los datos correctos
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


private fun appendDigit(current: String, digit: String): String {
    if (current == "0.00") return digit
    val parts = current.split(".")
    if (parts.size == 2 && parts[1].length >= 2) return current
    return current + digit
}

private fun deleteDigit(current: String): String {
    if (current.length <= 1) return "0.00"
    return current.dropLast(1)
}