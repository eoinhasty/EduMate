package ie.eoinhasty.edumate.ui.maps

/**
 * MeetingLocations.kt
 * This file defines the `MeetingLocations` class for representing meeting locations with a name and coordinates.
 *
 * Features:
 * - Stores name, latitude, and longitude of a meeting location.
 * - Provides a utility method to return the location as a LatLng object.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import com.google.android.gms.maps.model.LatLng

/**
 * Represents a meeting location with a name, latitude, and longitude.
 *
 * @property name The name of the location.
 * @property latitude The latitude coordinate of the location. Defaults to 0.0 if null.
 * @property longitude The longitude coordinate of the location. Defaults to 0.0 if null.
 */
class MeetingLocations(
    val name: String,
    val latitude: Double?,
    val longitude: Double?
) {
    /**
     * Returns the position of the location as a LatLng object.
     * If latitude or longitude is null, defaults to (0.0, 0.0).
     *
     * @return A LatLng object representing the location.
     */
    fun position() : LatLng {
        return LatLng(latitude ?: 0.0, longitude ?: 0.0)
    }
}