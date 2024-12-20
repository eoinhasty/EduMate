package ie.eoinhasty.edumate.ui.maps
/**
 * ExploreViewModel.kt
 * This file defines the ViewModel for managing and providing data for the Explore screen.
 *
 * Features:
 * - Provides a list of predefined meeting locations.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import androidx.lifecycle.ViewModel

/**
 * ViewModel for the Explore screen, responsible for loading and managing meeting locations.
 */
class ExploreViewModel: ViewModel() {
    /**
     * Loads a predefined list of meeting locations.
     * These locations are represented as a list of `MeetingLocations` objects, each with a name and coordinates.
     *
     * @return A list of meeting locations.
     */
    fun loadMeetingLocations(): List<MeetingLocations> {
        return listOf(
            MeetingLocations("Student Union", 52.674044449792454, -8.64541217685295),
            MeetingLocations("Library", 52.67460919335351, -8.64822409242108),
            MeetingLocations("Cafeteria", 52.67538338332762, -8.647179148364275)
        )
    }
}