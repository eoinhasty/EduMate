package ie.eoinhasty.edumate

/**
 * MainActivity.kt
 * Entry point for the EduMate application.
 *
 * Features:
 * - Sets up the application theme and edge-to-edge display.
 * - Initializes and manages the navigation graph for the app.
 * - Requests and manages location permissions using Accompanist.
 *
 * Components:
 * - `MapPermissions`: Handles location permission requests and updates the LocationViewModel.
 * - `BuildNavigationGraph`: Initializes and manages the app's navigation flow.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import ie.eoinhasty.edumate.ui.authentication.LoginScreen
import ie.eoinhasty.edumate.ui.maps.LocationViewModel
import ie.eoinhasty.edumate.ui.navigation.BuildNavigationGraph
import ie.eoinhasty.edumate.ui.theme.EduMateTheme

/**
 * The main activity for the EduMate application.
 */
class MainActivity : ComponentActivity() {
    private val locationViewModel: LocationViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduMateTheme(dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    MapPermissions(locationViewModel)
                    BuildNavigationGraph()
                }
            }
        }
    }

    /**
     * Handles the location permissions for the app and updates the LocationViewModel.
     *
     * @param locationViewModel The ViewModel managing location-related states.
     */
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun MapPermissions(locationViewModel: LocationViewModel) {
        val locationPermissionState = rememberMultiplePermissionsState(
            permissions = listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
        locationViewModel.updatePermissionState(locationPermissionState)
        LaunchedEffect(true) {
            locationPermissionState.launchMultiplePermissionRequest()
        }
    }
}