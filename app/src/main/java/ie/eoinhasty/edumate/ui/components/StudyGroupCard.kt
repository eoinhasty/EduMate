package ie.eoinhasty.edumate.ui.components

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

@Composable
fun StudyGroupCard(
    icon: ImageVector,
    isOnline: Boolean,
    heading: String,
    subheading: String,
    nextSession: String,
    buttonText: String,
    buttonColor: Color,
    onButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )

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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isOnline) Icons.Filled.Computer else Icons.Filled.Place,
                        contentDescription = if (isOnline) "Online" else "In Person"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = nextSession,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

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
            nextSession = "Thursday at 4 PM",
            buttonText = "Leave",
            buttonColor = MaterialTheme.colorScheme.error,
            onButtonClick = {},
            isOnline = true
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
            nextSession = "Monday at 3 PM",
            buttonText = "Joined",
            buttonColor = SkyBlue,
            onButtonClick = {},
            isOnline = false
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
            nextSession = "Friday at 2 PM",
            buttonText = "Join",
            buttonColor = MossGreen,
            onButtonClick = {},
            isOnline = true
        )
    }
}
