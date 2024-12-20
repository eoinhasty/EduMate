package ie.eoinhasty.edumate.ui.maps

/**
 * ExploreScreen.kt
 * This file defines the Explore screen, which displays a Google Map with markers for meeting locations.
 *
 * Features:
 * - Displays a map with customizable settings.
 * - Places markers on the map for predefined meeting locations.
 * - Centers the camera on the user's last known location if permissions are granted.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import ie.eoinhasty.edumate.ui.BaseContainer

/**
 * Main entry point for the Explore screen, displaying a map with meeting locations.
 *
 * @param navController The NavHostController to handle navigation.
 * @param exploreViewModel The view model providing meeting location data.
 * @param locationViewModel The view model managing location permissions and user location.
 */
@Composable
fun ExploreScreen(
    navController: NavHostController,
    exploreViewModel: ExploreViewModel,
    locationViewModel: LocationViewModel
) {
    // Load meeting locations from the view model
    val meetingLocations = exploreViewModel.loadMeetingLocations()

    BaseContainer(
        navController = navController,
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {
            ExploreMapScreen(
                currentLocation = locationViewModel.lastLocation,
                getLastLocation = { locationViewModel.getLastLocation() },
                hasLocationPermission = { locationViewModel.hasPermission() },
                locationsList = meetingLocations
            )
        }
    }
}

/**
 * Displays the Google Map with properties, settings, and markers.
 *
 * @param modifier Modifier for customizing layout.
 * @param currentLocation The user's last known location.
 * @param getLastLocation Function to fetch the user's last known location.
 * @param hasLocationPermission Function to check if location permissions are granted.
 * @param locationsList List of predefined meeting locations to display as markers.
 */
@Composable
private fun ExploreMapScreen(
    modifier: Modifier = Modifier,
    currentLocation: Location?,
    getLastLocation: () -> Unit = {},
    hasLocationPermission: () -> Boolean = { false },
    locationsList: List<MeetingLocations> = listOf()
) {
    val cameraPositionState = rememberCameraPositionState {}
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = true,
                myLocationButtonEnabled = true,
                rotationGesturesEnabled = true,
                scrollGesturesEnabled = true,
                scrollGesturesEnabledDuringRotateOrZoom = true,
                tiltGesturesEnabled = true,
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true
            )
        )
    }
    val properties by remember {
        mutableStateOf(
            MapProperties(
                isBuildingEnabled = false,
                isMyLocationEnabled = hasLocationPermission(),
                isIndoorEnabled = false,
                isTrafficEnabled = false,
                mapType = MapType.NORMAL,
                maxZoomPreference = 21f,
                minZoomPreference = 3f
            )
        )
    }

    GoogleMap(
        modifier = modifier,
        properties = properties,
        uiSettings = uiSettings,
        cameraPositionState = cameraPositionState
    ) {
        getLastLocation()
        val location = currentLocation?.let {
            LatLng(52.67442204063236, -8.648923419156366)
        }
        for (location in locationsList) {
            DisplayMarker(location)
        }
        location?.let {
            cameraPositionState.move(
                update = CameraUpdateFactory.newLatLngZoom(it, 12f)
            )
        }
    }
}

/**
 * Places a marker on the map with a custom info window.
 *
 * @param location The location data for the marker.
 */
@Composable
private fun DisplayMarker(
    location: MeetingLocations
) {
    MarkerInfoWindowContent(
        rememberMarkerState(position = location.position()),
        title = location.name
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .size(width = 250.dp, height = 50.dp)
                .padding(8.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 6.dp),
                text = location.name,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}