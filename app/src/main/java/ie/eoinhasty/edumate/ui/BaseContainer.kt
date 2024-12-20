package ie.eoinhasty.edumate.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import ie.eoinhasty.edumate.ui.navigation.Screen
import ie.eoinhasty.edumate.ui.navigation.navbar.BottomNavBar
import ie.eoinhasty.edumate.ui.navigation.navbar.TopNavBar

@Composable
fun BaseContainer(
    navController: NavHostController,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {}
) {
    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        content = { innerPadding -> pageContent(innerPadding) },
        topBar = {
                val currentRoute = navController.currentDestination?.route

                TopNavBar(
                    title = when (currentRoute) {
                        Screen.MyGroups.route -> "My Groups"
                        Screen.GroupDetails.route -> "Group Details"
                        Screen.AddGroup.route -> "Add Group"
                        Screen.Discover.route -> "Discover Groups"
                        else -> "EduMate"
                    },
                    showBackButton = currentRoute != Screen.Discover.route && currentRoute != Screen.MyGroups.route && currentRoute != Screen.Explore.route,
                    showActionButton = navController.currentDestination?.route == Screen.Discover.route,
                    onBackClick = { navController.popBackStack() },
                    onActionClick = {
                        if(currentRoute == Screen.Discover.route) {
                            navController.navigate(Screen.AddGroup.route)
                        }
                    }
                )
        }
    )
}