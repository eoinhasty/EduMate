package ie.eoinhasty.edumate.ui.studygroups

/**
 * StudyGroupDetailScreen.kt
 * Displays the details of a specific study group, including its description, members, and study sessions.
 *
 * Features:
 * - Displays group details: name, description, meeting type, year, schedule, category, and members.
 * - Shows study sessions related to the group.
 * - Allows the group creator to add new study sessions.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ie.eoinhasty.edumate.data.studygroups.StudyGroup
import ie.eoinhasty.edumate.data.studysessions.StudySession
import ie.eoinhasty.edumate.ui.BaseContainer
import ie.eoinhasty.edumate.ui.components.StudySessionCard

/**
 * Entry point for the Study Group Detail screen.
 *
 * @param studyGroup The StudyGroup object containing group details.
 * @param navController The NavHostController to handle navigation.
 * @param studyGroupViewModel The ViewModel managing study group and session data.
 */
@Composable
fun StudyGroupDetailScreen(
    studyGroup: StudyGroup,
    navController: NavHostController,
    studyGroupViewModel: StudyGroupViewModel
) {
    BaseContainer(
        navController = navController
    ) { innerPadding ->
        Surface (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            StudyGroupScreenContent(
                studyGroup,
                studyGroupViewModel,
                navController
            )
        }
    }
}

/**
 * Content composable for the Study Group Detail screen.
 * Displays group information and a list of associated study sessions.
 *
 * @param studyGroup The StudyGroup object containing group details.
 * @param studyGroupViewModel The ViewModel managing study group and session data.
 * @param navController The NavHostController to handle navigation.
 */
@Composable
fun StudyGroupScreenContent(
    studyGroup: StudyGroup,
    studyGroupViewModel: StudyGroupViewModel,
    navController: NavHostController
) {
    // State variables for members, creator, sessions and group creator status
    var memberNames by remember { mutableStateOf("Loading members...") }
    var createdBy by remember { mutableStateOf("Loading name...") }
    var studySessions by remember { mutableStateOf<List<StudySession>>(emptyList()) }
    var isCurrentUserGroupCreator by remember { mutableStateOf(false) }

    // Fetch group members, creator info, and study sessions when screen is displayed
    LaunchedEffect(studyGroup.groupId) {
        studyGroupViewModel.getGroupMembersFromFirestore(
            groupId = studyGroup.groupId,
            onSuccess = { users ->
                memberNames = users.joinToString(", ") { "${it.firstName} ${it.lastName}" }
                studyGroupViewModel.getUserByID(
                    userId = studyGroup.createdBy,
                    onSuccess = { user ->
                        createdBy = "${user.firstName} ${user.lastName}"
                    },
                    onFailure = {
                        createdBy = "Failed to load name."
                    }
                )
            },
            onFailure = {
                memberNames = "Failed to load members."
                createdBy = "Failed to load name."
            }
        )

        studyGroupViewModel.fetchStudySessions(
            groupId = studyGroup.groupId,
            onSuccess = { sessions ->
                studySessions = sessions
            },
            onFailure = {
                studySessions = emptyList()
            }
        )

        studyGroupViewModel.getCurrentUser()
        isCurrentUserGroupCreator = studyGroup.createdBy == studyGroupViewModel.currentUser.value?.userId
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //Group Name
        Text(
            text = studyGroup.groupName,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        //Description
        Text(
            text = "Description: ${studyGroup.description}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        //Meeting Type
        Text(
            text = "Meeting Type: ${studyGroup.meetingType}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        //Year
        Text(
            text = "Year: ${studyGroup.year}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        //Schedule
        Text(
            text = "Schedule: ${studyGroup.schedule}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        //Category
        Text(
            text = "Category: ${studyGroup.category}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        //Members Count
        Text(
            text = "Members(${studyGroup.memberCount}): $memberNames",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        //Created By
        Text(
            text = "Created by: $createdBy",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        //Sessions
        Text(
            text = "Study Sessions:",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (studySessions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(studySessions) { session ->
                    StudySessionCard(
                        sessionTitle = session.sessionTitle,
                        sessionDescription = session.sessionDescription,
                        sessionDateTime = session.sessionDateTime,
                        location = session.location,
                        isOnline = session.isOnline,
                    )
                }
            }
        } else {
            Text(
                text = "No study sessions available.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if(isCurrentUserGroupCreator) {
            //Add Session Button
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate(
                        "addSession/${studyGroup.groupId}"
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),

                ) {
                Text(text = "Add Study Session")
            }
        } else {
            Text(
                text = "Only the group creator can add sessions.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}