package com.resolum.intiva.core.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class representing a navigation item in the bottom navigation bar.
 *
 * @param route The navigation route associated with this item.
 * @param label The display label for the navigation item.
 * @param icon The icon to be shown for this navigation item.
 * @param contentDescription A description for accessibility services.
 */
data class NavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
)