package com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.CategoryUiState

private val BrandPurple = Color(0xFF534AB7)
private val AccentGreen = Color(0xFFCDEB45)
private val NeutralGray = Color(0xFF78767E)

private data class CategoryIconOption(
    val name: String,
    val icon: ImageVector
)

private val categoryIcons = listOf(
    CategoryIconOption("shopping-cart", Icons.Outlined.ShoppingCart),
    CategoryIconOption("car", Icons.Outlined.DirectionsCar),
    CategoryIconOption("education", Icons.Outlined.School),
    CategoryIconOption("health", Icons.Outlined.MedicalServices),
    CategoryIconOption("food", Icons.Outlined.Restaurant),
    CategoryIconOption("wallet", Icons.Outlined.AccountBalanceWallet),
    CategoryIconOption("book", Icons.Outlined.MenuBook),
    CategoryIconOption("clothes", Icons.Outlined.Checkroom),
    CategoryIconOption("movie", Icons.Outlined.Movie),
    CategoryIconOption("heart", Icons.Outlined.FavoriteBorder)
)

private val categoryColors = listOf(
    "#D8CFF7",
    "#CDEB45",
    "#F6D7C3",
    "#F8D1D1",
    "#D7E3FF",
    "#FFE6B3"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryCreateScreen(
    uiState: CategoryUiState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onIconSelected: (String) -> Unit,
    onColorSelected: (String) -> Unit,
    onCreateCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLoading = uiState.createCategoryState is UiState.Loading

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .padding(24.dp)
    ) {
        Text(
            text = "Nueva categoría",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = IntivaColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Crea una categoría para organizar mejor tus gastos.",
            style = MaterialTheme.typography.bodyMedium,
            color = NeutralGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.name,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nombre") },
            placeholder = { Text("Ej. Supermercado") },
            singleLine = true,
            isError = uiState.nameError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandPurple,
                unfocusedBorderColor = Color(0xFFD8D3E7),
                errorBorderColor = IntivaColors.ErrorRed
            )
        )

        uiState.nameError?.let { message ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message,
                color = IntivaColors.ErrorRed,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.description,
            onValueChange = onDescriptionChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Descripción") },
            placeholder = { Text("Ej. Gastos recurrentes") },
            minLines = 2,
            maxLines = 3,
            textStyle = LocalTextStyle.current,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandPurple,
                unfocusedBorderColor = Color(0xFFD8D3E7)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Icono",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = NeutralGray
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            categoryIcons.forEach { option ->
                val selected = uiState.selectedIcon == option.name

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            color = if (selected) BrandPurple else Color(0xFFF2F0FA),
                            shape = CircleShape
                        )
                        .clickable { onIconSelected(option.name) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = option.name,
                        tint = if (selected) Color.White else NeutralGray,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Color",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = NeutralGray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            categoryColors.forEach { color ->
                val selected = uiState.selectedColor == color

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(android.graphics.Color.parseColor(color)), CircleShape)
                        .clickable { onColorSelected(color) },
                    contentAlignment = Alignment.Center
                ) {
                    if (selected) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .background(Color.White, CircleShape)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (val state = uiState.createCategoryState) {
            is UiState.Error -> {
                Text(
                    text = state.message,
                    color = IntivaColors.ErrorRed,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            else -> Unit
        }

        Button(
            onClick = onCreateCategory,
            enabled = uiState.isCreateFormValid && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentGreen,
                disabledContainerColor = Color(0xFFE8E6F1)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = IntivaColors.TextPrimary
                )
            } else {
                Text(
                    text = "Guardar categoría",
                    color = IntivaColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}