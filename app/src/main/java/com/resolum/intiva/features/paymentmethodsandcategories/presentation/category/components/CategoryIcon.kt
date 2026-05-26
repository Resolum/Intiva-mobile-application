package com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Slideshow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable function to display an icon for a category based on the provided [iconName].
 *
 * This function maps specific icon names to corresponding Material Icons. If the provided
 * [iconName] does not match any predefined cases, a default category icon is displayed.
 *
 * @param iconName The name of the icon to display, which determines the specific icon shown.
 */
@Composable
fun CategoryIcon(iconName: String) {
    val icon = when (iconName) {
        "wallet"        -> Icons.Outlined.AccountBalanceWallet
        "shopping-cart" -> Icons.Outlined.ShoppingCart
        "car"           -> Icons.Outlined.DirectionsCar
        "heart"         -> Icons.Outlined.FavoriteBorder
        "book"          -> Icons.Outlined.MenuBook
        "food"          -> Icons.Outlined.Restaurant
        "clothes"       -> Icons.Outlined.Checkroom
        "movie"         -> Icons.Outlined.Slideshow
        "education"     -> Icons.Outlined.School
        "health"        -> Icons.Outlined.MedicalServices
        else            -> Icons.Outlined.Category
    }

    Icon(
        imageVector = icon,
        contentDescription = iconName,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(24.dp)
    )
}