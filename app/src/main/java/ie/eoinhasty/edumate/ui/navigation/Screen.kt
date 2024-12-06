package ie.eoinhasty.edumate.ui.navigation

sealed class Screen(
    val route : String
) {
    data object MyGroups: Screen("myGroups")
    data object Explore: Screen("explore")
    data object Discover: Screen("discover")
    data object Notifications: Screen("notifications")
}

val screens = listOf(
    Screen.MyGroups,
    Screen.Explore,
    Screen.Discover,
    Screen.Notifications
)