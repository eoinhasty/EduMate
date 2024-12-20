package ie.eoinhasty.edumate.ui.navigation.navbar;

/**
 * NavBarIcon.kt
 * This file defines the `NavBarIcon` data class, which represents the icon and label for navigation bar items.
 *
 * Features:
 * - Associates an icon (ImageVector) and label (String) for a navigation item.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class representing an icon and label for a navigation bar item.
 *
 * @property filledIcon The icon representing the navigation item.
 * @property label The label text for the navigation item.
 */
data class NavBarIcon(
        val filledIcon: ImageVector,
        val label: String
)
