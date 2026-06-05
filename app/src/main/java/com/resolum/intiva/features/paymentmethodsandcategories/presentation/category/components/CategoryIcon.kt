package com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Laptop
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Slideshow
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CategoryIcon(
    iconName: String,
    tintColor: Color = Color.White
) {
    val icon = when (iconName.normalizedCategoryIconName()) {
        "briefcase" -> Icons.Outlined.BusinessCenter
        "laptop" -> Icons.Outlined.Laptop
        "store" -> Icons.Outlined.Store
        "trending_up" -> Icons.AutoMirrored.Outlined.TrendingUp
        "home" -> Icons.Outlined.Home
        "shield" -> Icons.Outlined.Shield
        "more_horiz" -> Icons.Outlined.MoreHoriz
        "wallet" -> Icons.Outlined.AccountBalanceWallet
        "shopping-cart" -> Icons.Outlined.ShoppingCart
        "car" -> Icons.Outlined.DirectionsCar
        "heart" -> Icons.Outlined.FavoriteBorder
        "book" -> Icons.AutoMirrored.Outlined.MenuBook
        "food" -> Icons.Outlined.Restaurant
        "clothes" -> Icons.Outlined.Checkroom
        "movie" -> Icons.Outlined.Slideshow
        "education" -> Icons.Outlined.School
        "health" -> Icons.Outlined.MedicalServices
        else -> Icons.Outlined.Category
    }

    Icon(
        imageVector = icon,
        contentDescription = iconName,
        tint = tintColor, // <-- Usamos el parámetro dinámico aquí
        modifier = Modifier.size(24.dp)
    )
}

private fun String.normalizedCategoryIconName(): String {
    val key = trim().lowercase().replace("-", "_")
    return when (key) {
        "restaurant", "local_restaurant", "fastfood" -> "food"
        "directions_car", "commute" -> "car"
        "local_hospital", "medical_services" -> "health"
        "shopping_bag", "shopping_cart" -> "shopping-cart"
        "school" -> "education"
        "menu_book" -> "book"
        "account_balance_wallet" -> "wallet"
        "business_center" -> "briefcase"
        "category" -> "more_horiz"
        else -> key
    }
}
