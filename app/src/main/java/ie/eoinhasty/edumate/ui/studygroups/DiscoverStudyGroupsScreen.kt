package ie.eoinhasty.edumate.ui.studygroups

/**
 * DiscoverStudyGroupsScreen.kt
 * Displays a list of available study groups for users to explore, join, or leave.
 *
 * Features:
 * - Displays a list of study groups with filter options by year.
 * - Allows users to join or leave a study group.
 * - Navigation to detailed view of a selected study group.
 * - Handles loading and error states during data fetch.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
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
 * Entry point for the Discover Study Groups screen, providing users with the ability to browse,
 * filter, join, and leave study groups.
 *
 * @param navController The NavHostController to handle navigation.
 * @param studyGroupViewModel The ViewModel managing the data and logic for study groups.
 */
@Composable
fun DiscoverStudyGroupsScreen(
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
            DiscoverStudyGroupsScreenContent(studyGroupViewModel, navController)
        }
    }
}

/**
 * Composable content for the Discover Study Groups screen.
 * Displays a list of study groups and provides filtering and action options.
 *
 * @param studyGroupViewModel The ViewModel managing the data and logic for study groups.
 * @param navController The NavHostController to handle navigation.
 */
@Composable
fun DiscoverStudyGroupsScreenContent(
    studyGroupViewModel: StudyGroupViewModel,
    navController: NavHostController
) {
    val studyGroups = studyGroupViewModel.studyGroups.collectAsState()
    val loading = studyGroupViewModel.loading.collectAsState()
    val error = studyGroupViewModel.error.collectAsState()

    val auth: FirebaseAuth = Firebase.auth
    val user = auth.currentUser

    val context = LocalContext.current

    var selectedYear by remember { mutableStateOf("All") }
    val years = listOf("All", "SD1", "SD2", "SD3", "SD4")

    LaunchedEffect(Unit) {
        studyGroupViewModel.fetchStudyGroups()
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
                    text = "No study groups found.\nCreate one!",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    // Filter by year dropdown
                    DropdownMenuField(
                        options = years,
                        selectedOption = selectedYear,
                        onOptionSelected = { selectedYear = it },
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val filteredGroups = if(selectedYear == "All")
                            studyGroups.value
                        else studyGroups.value.filter {
                            it.year == selectedYear
                        }

                        if(filteredGroups.isEmpty()) {
                            item {
                                Text(
                                    text = "No study groups found for $selectedYear",
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        } else {
                            items(filteredGroups) { studyGroup ->
                                StudyGroupCard(
                                    icon = studyGroupViewModel.getIconForName(studyGroup.iconName),
                                    isOnline = studyGroup.meetingType == "Online",
                                    heading = studyGroup.groupName,
                                    subheading = studyGroup.memberCount.toString() + " member(s) | ${studyGroup.year}",
                                    buttonColor = if (studyGroup.members.contains(user?.uid)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                    buttonText = if (studyGroup.members.contains(user?.uid)) "Leave" else "Join",
                                    onButtonClick = {
                                        if (studyGroup.members.contains(user?.uid))
                                            studyGroupViewModel.leaveStudyGroup(
                                                studyGroup = studyGroup,
                                                userId = user?.uid ?: "",
                                                onSuccess = {
                                                    studyGroupViewModel.fetchStudyGroups()
                                                },
                                                onFailure = { error ->
                                                    Toast.makeText(
                                                        context,
                                                        error,
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                }
                                            )
                                        else
                                            studyGroupViewModel.joinStudyGroup(
                                                studyGroup = studyGroup,
                                                userId = user?.uid ?: "",
                                                onSuccess = {
                                                    studyGroupViewModel.fetchStudyGroups()
                                                },
                                                onFailure = { error ->
                                                    Toast.makeText(
                                                        context,
                                                        error,
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                }
                                            )
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
    }
}