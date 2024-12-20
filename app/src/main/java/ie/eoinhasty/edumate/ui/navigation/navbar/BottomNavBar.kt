package ie.eoinhasty.edumate.ui.navigation.navbar

/**
 * BottomNavBar.kt
 * This file defines the `BottomNavBar` composable, which provides a bottom navigation bar for navigating between app screens.
 *
 * Features:
 * - Navigation items include Explore, Discover, My Groups, and Contact.
 * - Highlights the selected item with appropriate styling.
 * - Integrates Material3 design principles.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ie.eoinhasty.edumate.ui.navigation.Screen
import ie.eoinhasty.edumate.ui.navigation.screens
import ie.eoinhasty.edumate.ui.theme.*

/**
 * Composable for the bottom navigation bar.
 *
 * @param navController The NavHostController for handling navigation actions.
 */
@Composable
fun BottomNavBar(
    navController: NavHostController,
) {
    // Map screen routes to their respective icons and labels.
    val icons = mapOf(
        Screen.Explore to NavBarIcon(
            filledIcon = Icons.Filled.Place,
            label = "Explore"
        ),
        Screen.Discover to NavBarIcon(
            filledIcon = Icons.Filled.Search,
            label = "Discover"
        ),
        Screen.MyGroups to NavBarIcon(
            filledIcon = Icons.Filled.Groups,
            label = "My Groups"
        ),
        Screen.Contact to NavBarIcon(
            filledIcon = Icons.Filled.Phone,
            label = "Contact"
        )
    )

    // Filter screens to include only those present in the icons map.
    val bottomNavScreens = screens.filter { icons.containsKey(it) }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavScreens.forEach { screen ->
            val isSelected = currentDestination?.route == screen.route

            val label = icons[screen]!!.label

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route)
                },
                icon = {
                    Icon(
                        imageVector = icons[screen]!!.filledIcon,
                        contentDescription = label,
                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                },
                label = {
                    Text(
                        text = label,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}

@Preview
@Composable
fun BottomNavBarLightPreview() {
    EduMateTheme(dynamicColor = false, darkTheme = false) {
        BottomNavBar(navController = rememberNavController())
    }
}

@Preview
@Composable
fun BottomNavBarDarkPreview() {
    EduMateTheme(dynamicColor = false, darkTheme = true) {
        BottomNavBar(navController = rememberNavController())
    }
}