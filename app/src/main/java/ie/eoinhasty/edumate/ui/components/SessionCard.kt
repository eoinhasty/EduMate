package ie.eoinhasty.edumate.ui.components

/**
 * StudySessionCard.kt
 * This file defines a composable UI component to display a study session card.
 *
 * Features:
 * - Displays session title, description, date, time, and location.
 * - Differentiates between online and in-person sessions with an icon.
 * - Adheres to Material Design guidelines with rounded corners and appropriate padding.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ie.eoinhasty.edumate.ui.theme.EduMateTheme

/**
 * Displays a card with study session details.
 *
 * @param sessionTitle The title of the study session.
 * @param sessionDescription A brief description of the session.
 * @param sessionDateTime The date and time of the session.
 * @param location The location of the session (or "Online" if applicable).
 * @param isOnline True if the session is online; false if in-person.
 */
@Composable
fun StudySessionCard(
    sessionTitle: String,
    sessionDescription: String,
    sessionDateTime: String,
    location: String,
    isOnline: Boolean,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(130.dp),
        shape = RoundedCornerShape(12.dp), // Rounded corners for the card
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f) // Occupies remaining space
            ) {
                // Title
                Text(
                    text = sessionTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // Truncate long titles
                )

                // Description
                Text(
                    text = sessionDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis // Truncate long descriptions
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Date, Time, and Location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isOnline) Icons.Filled.Computer else Icons.Filled.Place,
                        contentDescription = if (isOnline) "Online" else "In Person"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$sessionDateTime | $location",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun StudySessionCardLightPreview() {
    EduMateTheme(
        darkTheme = false,
        dynamicColor = false,
    ) {
        StudySessionCard(
            sessionTitle = "Study Session Title",
            sessionDescription = "This is a description of the study session. It can be quite long and should wrap to the next line if it is too long.",
            sessionDateTime = "Monday, 12th July at 3:00pm",
            location = "Online",
            isOnline = true
        )
    }
}