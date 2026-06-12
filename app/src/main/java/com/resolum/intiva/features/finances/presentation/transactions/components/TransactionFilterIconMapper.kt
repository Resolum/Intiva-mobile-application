package com.resolum.intiva.features.finances.presentation.transactions.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.Locale

fun iconForTransactionCategory(name: String): ImageVector {
    val normalized = name.lowercase(Locale.ROOT)
    return when {
        "aliment" in normalized || "comida" in normalized -> Icons.Default.Restaurant
        "transport" in normalized || "movilidad" in normalized -> Icons.Default.DirectionsCar
        "vivienda" in normalized || "hogar" in normalized || "casa" in normalized -> Icons.Default.Home
        "salud" in normalized -> Icons.Default.FavoriteBorder
        "compra" in normalized -> Icons.Default.ShoppingBag
        "educ" in normalized -> Icons.Default.School
        else -> Icons.Default.MoreHoriz
    }
}
