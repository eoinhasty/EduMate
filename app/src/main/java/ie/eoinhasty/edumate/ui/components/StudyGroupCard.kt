package ie.eoinhasty.edumate.ui.components

/**
 * StudyGroupCard.kt
 * This file defines a composable UI component for displaying a study group card.
 *
 * Features:
 * - Displays study group details including name, number of members, and session details.
 * - Indicates whether the group is for online or in-person sessions with an icon.
 * - Includes an action button for joining, leaving, or viewing study groups.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ie.eoinhasty.edumate.ui.theme.EduMateTheme
import ie.eoinhasty.edumate.ui.theme.MossGreen
import ie.eoinhasty.edumate.ui.theme.SkyBlue

/**
 * Displays a card with study group details and an action button.
 *
 * @param icon The icon representing the study group.
 * @param isOnline Indicates if the study group is online.
 * @param heading The main title of the study group (e.g., group name).
 * @param subheading Additional details such as member count.
 * @param buttonText The text displayed on the action button.
 * @param buttonColor The background color of the action button.
 * @param onButtonClick Callback for when the action button is clicked.
 * @param onClick Callback for when the card itself is clicked.
 */
@Composable
fun StudyGroupCard(
    icon: ImageVector,
    isOnline: Boolean,
    heading: String,
    subheading: String,
    buttonText: String,
    buttonColor: Color,
    onButtonClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(100.dp), // Fixed card height
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Icon representing the study group
            Image(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )

            // Text content: Heading and subheading
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = heading,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = subheading,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Icon and text for online/in-person indicator
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isOnline) Icons.Filled.Computer else Icons.Filled.Place,
                        contentDescription = if (isOnline) "Online" else "In Person"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isOnline) "Online" else "In Person",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Action button
            Button(
                onClick = onButtonClick,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .height(40.dp)
                    .width(95.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = buttonText,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
@Preview
fun StudyGroupCardPreview() {
    EduMateTheme {
        StudyGroupCard(
            icon = Icons.Filled.AccountTree,
            heading = "Data Structures & Algs",
            subheading = "8 Members | Next session:",
            buttonText = "Leave",
            buttonColor = MaterialTheme.colorScheme.error,
            onButtonClick = {},
            isOnline = true,
            onClick = {}
        )
    }
}

@Composable
@Preview
fun StudyGroupCardJoinedPreview() {
    EduMateTheme {
        StudyGroupCard(
            icon = Icons.Filled.PhoneAndroid,
            heading = "Adv Mobile App & Design",
            subheading = "5 Members | Next session:",
            buttonText = "Joined",
            buttonColor = SkyBlue,
            onButtonClick = {},
            isOnline = false,
            onClick = {}
        )
    }
}

@Composable
@Preview
fun StudyGroupCardJoinPreview() {
    EduMateTheme {
        StudyGroupCard(
            icon = Icons.Filled.Settings,
            heading = "Machine Learning Basics",
            subheading = "6 Members | Next session:",
            buttonText = "Join",
            buttonColor = MossGreen,
            onButtonClick = {},
            isOnline = true,
            onClick = {}
        )
    }
}
