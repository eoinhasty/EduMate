package ie.eoinhasty.edumate.ui.navigation

/**
 * Screen.kt
 * Defines the navigation routes for the EduMate app using a sealed class.
 *
 * Features:
 * - Encapsulates navigation routes in a type-safe manner.
 * - Supports dynamic arguments for routes such as groupId.
 * - Provides a list of all screens for easy management in navigation components.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

/**
 * Represents the different screens in the EduMate app.
 *
 * @property route The navigation route as a string.
 */
sealed class Screen(
    val route : String
) {
    data object MyGroups: Screen("myGroups")
    data object Explore: Screen("explore")
    data object Discover: Screen("discover")
    data object Contact: Screen("contact")
    data object Login: Screen("login")
    data object Register: Screen("register")
    data object AddGroup: Screen("addGroup")
    data object GroupDetails: Screen("groupDetails/{groupId}")
    data object AddSession: Screen("addSession/{groupId}")
}

val screens = listOf(
    Screen.Explore,
    Screen.Discover,
    Screen.MyGroups,
    Screen.Contact,
    Screen.Login,
    Screen.Register,
    Screen.AddGroup,
    Screen.GroupDetails,
    Screen.AddSession
)