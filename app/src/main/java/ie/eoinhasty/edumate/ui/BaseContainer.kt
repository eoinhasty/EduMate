package ie.eoinhasty.edumate.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import ie.eoinhasty.edumate.ui.navigation.navbar.BottomNavBar

@Composable
fun BaseContainer(
    navController: NavHostController,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {}

) {
    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        content = { innerPadding -> pageContent(innerPadding) }
    )
}