package ie.eoinhasty.edumate.ui.studygroups

/**
 * MyStudyGroupScreen.kt
 * Displays a list of study groups the user has joined, with options to view details or leave groups.
 *
 * Features:
 * - Fetches and displays the study groups the logged-in user has joined.
 * - Allows users to leave groups.
 * - Handles loading and error states.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import ie.eoinhasty.edumate.ui.BaseContainer
import ie.eoinhasty.edumate.ui.components.StudyGroupCard

/**
 * Entry point for the My Study Groups screen, showing groups the user has joined.
 *
 * @param navController The NavHostController to handle navigation.
 * @param studyGroupViewModel The ViewModel managing study group data and logic.
 */
@Composable
fun MyStudyGroupScreen(
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
            MyStudyGroupScreenContent(navController, studyGroupViewModel)
        }
    }
}

/**
 * Composable content for the My Study Groups screen.
 * Displays a list of groups the user has joined with options to view details or leave.
 *
 * @param navController The NavHostController to handle navigation.
 * @param studyGroupViewModel The ViewModel managing study group data and logic.
 */
@Composable
private fun MyStudyGroupScreenContent (navController: NavHostController, studyGroupViewModel: StudyGroupViewModel) {
    Log.d("MyStudyGroupScreen", "Rendering MyStudyGroupScreen")

    // Collect study group data from the ViewModel
    val studyGroups = studyGroupViewModel.studyGroups.collectAsState()
    val loading = studyGroupViewModel.loading.collectAsState()
    val error = studyGroupViewModel.error.collectAsState()

    val auth: FirebaseAuth = Firebase.auth
    val user = auth.currentUser

    val context = LocalContext.current

    // Fetch study groups the user has joined when the screen is displayed
    LaunchedEffect(Unit) {
        if (user != null) {
            studyGroupViewModel.fetchJoinedStudyGroups(user.uid)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        when {
            loading.value -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            error.value != null -> {
                Text(
                    text = "Error: ${error.value}",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            studyGroups.value.isEmpty() -> {
                Text(
                    text = "No study groups found.\n Check the Discover tab to find new groups.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                LazyColumn (
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(studyGroups.value) { studyGroup ->
                        StudyGroupCard(
                            icon = studyGroupViewModel.getIconForName(studyGroup.iconName),
                            isOnline = studyGroup.meetingType == "Online",
                            heading = studyGroup.groupName,
                            subheading = studyGroup.memberCount.toString() + " member(s) | ${studyGroup.year}",
                            buttonColor = MaterialTheme.colorScheme.error,
                            buttonText = "Leave",
                            onButtonClick = {
                                if(studyGroup.members.contains(user?.uid)) {
                                    studyGroupViewModel.leaveStudyGroup(
                                        studyGroup = studyGroup,
                                        userId = user?.uid ?: "",
                                        onSuccess = {
                                            studyGroupViewModel.fetchJoinedStudyGroups(user?.uid ?: "")
                                        },
                                        onFailure = {
                                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                } else {
                                    Toast.makeText(context, "You are not a member of this group", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onClick = {
                                navController.navigate(
                                    "groupDetails/${studyGroup.groupId}"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}