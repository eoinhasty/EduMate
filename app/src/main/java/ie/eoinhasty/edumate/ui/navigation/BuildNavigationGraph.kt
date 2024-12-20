package ie.eoinhasty.edumate.ui.navigation

/**
 * BuildNavigationGraph.kt
 * This file defines the navigation graph for the EduMate app using Jetpack Compose Navigation.
 *
 * Features:
 * - Handles navigation between various screens of the app.
 * - Utilizes ViewModels for dependency injection and state management.
 * - Provides navigation for login, signup, study groups, sessions, maps, and contact functionalities.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ie.eoinhasty.edumate.ui.authentication.LoginScreen
import ie.eoinhasty.edumate.ui.authentication.LoginViewModel
import ie.eoinhasty.edumate.ui.authentication.SignUpScreen
import ie.eoinhasty.edumate.ui.authentication.SignupViewModel
import ie.eoinhasty.edumate.ui.contact.ContactUsScreen
import ie.eoinhasty.edumate.ui.maps.ExploreScreen
import ie.eoinhasty.edumate.ui.maps.ExploreViewModel
import ie.eoinhasty.edumate.ui.maps.LocationViewModel
import ie.eoinhasty.edumate.ui.studygroups.AddStudyGroupScreen
import ie.eoinhasty.edumate.ui.studygroups.AddStudySessionScreen
import ie.eoinhasty.edumate.ui.studygroups.DiscoverStudyGroupsScreen
import ie.eoinhasty.edumate.ui.studygroups.MyStudyGroupScreen
import ie.eoinhasty.edumate.ui.studygroups.StudyGroupDetailScreen
import ie.eoinhasty.edumate.ui.studygroups.StudyGroupViewModel

/**
 * Composable function that builds the navigation graph for the EduMate app.
 *
 * @param studyGroupViewModel ViewModel for managing study groups.
 * @param signupViewModel ViewModel for managing signup state.
 * @param loginViewModel ViewModel for managing login state.
 * @param exploreViewModel ViewModel for managing meeting locations in Explore screen.
 * @param locationViewModel ViewModel for managing user location.
 */
@Composable
fun BuildNavigationGraph(
    studyGroupViewModel: StudyGroupViewModel = viewModel(),
    signupViewModel: SignupViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel(),
    exploreViewModel: ExploreViewModel = viewModel(),
    locationViewModel: LocationViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                navController,
                loginViewModel,
                onLoginSuccess = { navController.navigate(Screen.MyGroups.route) })
        }
        composable(Screen.Register.route) {
            SignUpScreen(navController, signupViewModel)
        }
        composable(Screen.MyGroups.route) {
            MyStudyGroupScreen(navController, studyGroupViewModel)
        }
        composable(Screen.AddGroup.route) {
            AddStudyGroupScreen(studyGroupViewModel, navController)
        }
        composable(Screen.Discover.route) {
            DiscoverStudyGroupsScreen(navController, studyGroupViewModel)
        }
        composable(Screen.GroupDetails.route) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")
            if (groupId == null) {
                navController.popBackStack()
            } else {
                studyGroupViewModel.getStudyGroupById(groupId)?.let { studyGroup ->
                    StudyGroupDetailScreen(
                        studyGroup = studyGroup,
                        navController = navController,
                        studyGroupViewModel = studyGroupViewModel
                    )
                }
            }
        }

        composable(Screen.AddSession.route) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")
            if (groupId == null) {
                navController.popBackStack()
            } else {
                AddStudySessionScreen(
                    studyGroupId = groupId,
                    navController = navController,
                    studyGroupViewModel = studyGroupViewModel
                )
            }
        }

        composable(Screen.Explore.route) {
            ExploreScreen(navController, exploreViewModel, locationViewModel)
        }

        composable(Screen.Contact.route) {
            ContactUsScreen(navController)
        }
    }
}