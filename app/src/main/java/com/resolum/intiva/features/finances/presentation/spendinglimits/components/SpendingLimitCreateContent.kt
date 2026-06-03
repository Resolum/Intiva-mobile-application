package com.resolum.intiva.features.finances.presentation.spendinglimits.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.finances.presentation.spendinglimits.SpendingLimitFrequency
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.components.CategoryGrid
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingLimitCreateContent(
    createState: UiState<com.resolum.intiva.features.finances.domain.models.SpendingLimit>,
    onClose: () -> Unit,
    onSave: (Category, String, SpendingLimitFrequency) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var amount by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf(SpendingLimitFrequency.MONTHLY) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nuevo Límite",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IntivaColors.PrimaryBrand
                )
            )
        },
        containerColor = Color(0xFFFAF7FF),
        bottomBar = {
            Button(
                onClick = {
                    selectedCategory?.let { category ->
                        onSave(category, amount, frequency)
                    }
                },
                enabled = selectedCategory != null && amount.isNotBlank() && createState !is UiState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .height(58.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = IntivaColors.PrimaryGreen,
                    contentColor = IntivaColors.TextPrimary,
                    disabledContainerColor = Color(0xFFE5E0EC),
                    disabledContentColor = IntivaColors.TextSecondary
                )
            ) {
                if (createState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = IntivaColors.TextPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Guardar Límite",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            item {
                StepLabel(text = "PASO 1: SELECCIONA UN OBJETIVO")
                SelectedCategoryCard(selectedCategory = selectedCategory)
                Spacer(modifier = Modifier.height(12.dp))
                CategoryGrid(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }

            item {
                StepLabel(
                    text = "PASO 2: MONTO MÁXIMO",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                AmountInputCard(
                    amount = amount,
                    onAmountChange = { value ->
                        amount = value.filter { it.isDigit() || it == '.' }
                    }
                )
            }

            item {
                StepLabel(text = "PASO 3: FRECUENCIA")
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SpendingLimitFrequency.entries.forEach { option ->
                        FrequencyButton(
                            frequency = option,
                            selected = frequency == option,
                            onClick = { frequency = option },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (createState is UiState.Error) {
                item {
                    Text(
                        text = createState.message,
                        color = IntivaColors.StatusError,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}