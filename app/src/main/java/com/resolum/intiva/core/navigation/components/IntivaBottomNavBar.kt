// IntivaBottomNavBar.kt
package com.resolum.intiva.core.navigation.components

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.navigation.model.NavItem
import com.resolum.intiva.core.navigation.routes.NavRoutes
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
    if (currentRoute !in NavRoutes.BOTTOM_NAV_ROUTES) return

    /** Define the navigation items for the bottom navigation bar. */
    val navItems = remember {
        listOf(
            NavItem(NavRoutes.HOME, "INICIO", Icons.Default.Home, "Home"),
            NavItem(
                NavRoutes.TRANSACTIONS,
                "TRANSACCIONES.",
                Icons.Default.Receipt,
                "Transactions"
            ),
            NavItem(NavRoutes.SAVINGS_GOALS, "METAS", Icons.Default.TrackChanges, "Savings Goals"),
            NavItem(NavRoutes.FAMILY, "FAMILIA", Icons.Default.Group, "Family"),
            NavItem(NavRoutes.PROFILE, "PERFIL", Icons.Default.Person, "Profile")
        )
    }

    /** Define custom colors for selected and unselected states of navigation items. */
    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor   = IntivaColors.IconPurple,
        selectedTextColor   = IntivaColors.IconPurple,
        unselectedIconColor = IntivaColors.TextSecondary,
        unselectedTextColor = IntivaColors.TextSecondary,
        indicatorColor      = Color.White
    )

    /** Render the bottom navigation bar with the defined items and colors. */
    NavigationBar(
        modifier       = modifier,
        containerColor = Color.White,
        contentColor   = IntivaColors.TextSecondary
    ) {
        navItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector        = item.icon,
                        contentDescription = item.contentDescription
                    )
                },
                label = {
                    Text(
                        text     = item.label,
                        fontSize = 9.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                },
                selected = currentRoute == item.route,
                onClick  = { onNavigate(item.route) },
                colors   = itemColors
            )
        }
    }
}