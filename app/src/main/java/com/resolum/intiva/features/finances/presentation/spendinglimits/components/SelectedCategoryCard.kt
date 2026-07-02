package com.resolum.intiva.features.finances.presentation.spendinglimits.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.components.CategoryIcon

@Composable
fun SelectedCategoryCard(selectedCategory: Category?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(parseColor(selectedCategory?.color, Color(0xFFFFE1B8)).copy(alpha = 0.45f)),
                contentAlignment = Alignment.Center
            ) {
                CategoryIcon(
                    iconName = selectedCategory?.icon ?: "category",
                    tintColor = parseColor(selectedCategory?.color, Color(0xFF9A5A00))
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = selectedCategory?.name ?: "Selecciona una categoría",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = IntivaColors.TextPrimary
                )
                Text(
                    text = selectedCategory?.description?.takeIf { it.isNotBlank() } ?: "Elige el objetivo del límite",
                    fontSize = 14.sp,
                    color = IntivaColors.TextSecondary
                )
            }
            Text(
                text = "Cambiar",
                color = IntivaColors.PrimaryBrand,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = IntivaColors.TextSecondary
            )
        }
    }
}

private fun parseColor(value: String?, fallback: Color): Color =
    runCatching { Color(value?.toColorInt() ?: return@runCatching fallback) }.getOrElse { fallback }
