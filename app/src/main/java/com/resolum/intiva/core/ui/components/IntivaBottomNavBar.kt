package com.resolum.intiva.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.resolum.intiva.core.ui.theme.IntivaColors

/**
 * Reusable bottom navigation bar for the Intiva application.
 *
 * @param currentRoute The currently active route in the navigation backstack.
 * @param onNavigate Callback triggered when a main tab is selected.
 * @param modifier Modifier applied to the outer layout container.
 */
@Composable
fun IntivaBottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // List of routes that should display the bottom navigation bar
    val bottomNavRoutes = listOf(
        "home",
        "transactions",
        "savings_goals",
        "family",
        "profile"
    )

    if (currentRoute in bottomNavRoutes) {
        NavigationBar(
            modifier = modifier,
            containerColor = Color.White,
            contentColor = IntivaColors.TextSecondary
        ) {
            // Home Tab
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("INICIO") },
                selected = currentRoute == "home",
                onClick = { onNavigate("home") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = IntivaColors.IconPurple,
                    selectedTextColor = IntivaColors.IconPurple,
                    unselectedIconColor = IntivaColors.TextSecondary,
                    unselectedTextColor = IntivaColors.TextSecondary,
                    indicatorColor = Color.White
                )
            )
            // Transactions Tab
            NavigationBarItem(
                icon = { Icon(Icons.Default.Receipt, contentDescription = "Transactions") },
                label = { Text("TRANSACCIONES") },
                selected = currentRoute == "transactions",
                onClick = { /* TODO */ },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = IntivaColors.IconPurple,
                    selectedTextColor = IntivaColors.IconPurple,
                    unselectedIconColor = IntivaColors.TextSecondary,
                    unselectedTextColor = IntivaColors.TextSecondary,
                    indicatorColor = Color.White
                )
            )
            // Savings Goals Tab
            NavigationBarItem(
                icon = { Icon(Icons.Default.TrackChanges, contentDescription = "Savings Goals") },
                label = { Text("METAS") },
                selected = currentRoute == "savings_goals",
                onClick = { onNavigate("savings_goals") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = IntivaColors.IconPurple,
                    selectedTextColor = IntivaColors.IconPurple,
                    unselectedIconColor = IntivaColors.TextSecondary,
                    unselectedTextColor = IntivaColors.TextSecondary,
                    indicatorColor = Color.White
                )
            )
            // Family Tab
            NavigationBarItem(
                icon = { Icon(Icons.Default.Group, contentDescription = "Family") },
                label = { Text("FAMILIA") },
                selected = currentRoute == "family",
                onClick = { /* TODO */ },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = IntivaColors.IconPurple,
                    selectedTextColor = IntivaColors.IconPurple,
                    unselectedIconColor = IntivaColors.TextSecondary,
                    unselectedTextColor = IntivaColors.TextSecondary,
                    indicatorColor = Color.White
                )
            )
            // Profile Tab
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("PERFIL") },
                selected = currentRoute == "profile",
                onClick = { /* TODO */ },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = IntivaColors.IconPurple,
                    selectedTextColor = IntivaColors.IconPurple,
                    unselectedIconColor = IntivaColors.TextSecondary,
                    unselectedTextColor = IntivaColors.TextSecondary,
                    indicatorColor = Color.White
                )
            )
        }
    }
}
