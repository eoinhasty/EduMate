package ie.eoinhasty.edumate.ui.navigation.navbar

/**
 * TopNavBar.kt
 * This file defines the `TopNavBar` composable, which represents the top navigation bar with customizable options.
 *
 * Features:
 * - Displays a title.
 * - Optionally shows a back button for navigation.
 * - Optionally includes an action button for additional functionality (e.g., "Add Group").
 * - Adapts to light and dark themes.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ie.eoinhasty.edumate.ui.theme.EduMateTheme

/**
 * Composable function that displays a top navigation bar.
 *
 * @param title The title displayed at the center of the bar.
 * @param showBackButton Whether to display the back button on the left.
 * @param showActionButton Whether to display an action button on the right.
 * @param onBackClick Action triggered when the back button is clicked.
 * @param onActionClick Action triggered when the action button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(
    title: String,
    showBackButton: Boolean,
    showActionButton: Boolean,
    onBackClick: () -> Unit = {},
    onActionClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (showActionButton) {
                IconButton(
                    onClick = onActionClick
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Group"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Preview
@Composable
fun TopNavBarLightPreview() {
    EduMateTheme(darkTheme = false, dynamicColor = false) {
        TopNavBar(title = "Title", showBackButton = true, showActionButton = true)
    }
}

@Preview
@Composable
fun TopNavBarDarkPreview() {
    EduMateTheme(darkTheme = true, dynamicColor = false) {
        TopNavBar(title = "Title", showBackButton = true, showActionButton = true)
    }
}