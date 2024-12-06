package ie.eoinhasty.edumate.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun BuildNavigationGraph(

){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.MyGroups.route){
//        composable(Screen.MyGroups.route) { MyStudyGroupsScreen(navController) }
//        composable(Screen.Explore.route) { ExploreScreen(navController)}
//        composable(Screen.Discover.route) { DiscoverScreen(navController)}
//        composable(Screen.Notifications.route) { NotificationsScreen(navController)}
    }
}