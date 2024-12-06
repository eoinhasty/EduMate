package ie.eoinhasty.edumate.ui.navigation.navbar

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
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

@Composable
fun BottomNavBar(
    navController: NavHostController,
) {
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
        Screen.Notifications to NavBarIcon(
            filledIcon = Icons.Filled.Notifications,
            label = "Notifications"
        )
    )


    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        screens.forEach { screen ->
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