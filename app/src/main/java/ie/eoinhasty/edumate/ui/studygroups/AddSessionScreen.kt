package ie.eoinhasty.edumate.ui.studygroups

/**
 * AddStudySessionScreen.kt
 * Allows users to add a new study session to a specific study group.
 *
 * Features:
 * - Input fields for session details: title, description, location, and meeting link.
 * - Online/In-person toggle with location selection for in-person sessions.
 * - Date and time picker restricted to the study group's schedule.
 * - Validation to ensure inputs are complete and within allowed schedule range.
 * - Save functionality to add the session and navigate back to the previous screen.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import android.app.TimePickerDialog
import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ie.eoinhasty.edumate.data.studygroups.StudyGroup
import ie.eoinhasty.edumate.data.studysessions.StudySession
import ie.eoinhasty.edumate.ui.BaseContainer
import ie.eoinhasty.edumate.ui.maps.ExploreViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Main entry point for the Add Study Session screen, allowing users to add a new session
 * to a specific study group.
 *
 * @param studyGroupId The unique identifier of the study group to which the session will be added.
 * @param navController The NavHostController to handle navigation.
 * @param studyGroupViewModel The ViewModel managing study group and session data.
 */

@Composable
fun AddStudySessionScreen(
    studyGroupId: String,
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
            AddStudySessionScreenContent(studyGroupId, navController, studyGroupViewModel)
        }
    }
}

/**
 * Composable content for the Add Study Session screen, containing input fields for session details.
 *
 * @param studyGroupId The unique identifier of the study group to which the session will be added.
 * @param navController The NavHostController to handle navigation.
 * @param studyGroupViewModel The ViewModel managing study group and session data.
 */
@Composable
fun AddStudySessionScreenContent(
    studyGroupId: String,
    navController: NavHostController,
    studyGroupViewModel: StudyGroupViewModel
) {
    val context = LocalContext.current
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    // State for input fields
    var sessionTitle by remember { mutableStateOf("") }
    var sessionDescription by remember { mutableStateOf("") }
    var sessionDateTime by remember { mutableStateOf<Date?>(null) }
    var location by remember { mutableStateOf("") }
    var meetingLink by remember { mutableStateOf("") }
    var isOnline by remember { mutableStateOf(true) }
    var studyGroup by remember { mutableStateOf(StudyGroup()) }
    var startDate by remember { mutableStateOf(Date()) }
    var endDate by remember { mutableStateOf(Date()) }

    // Fetch study group details and parse schedule
    LaunchedEffect(Unit) {
        studyGroup = studyGroupViewModel.getStudyGroupById(studyGroupId)!!
        studyGroup?.schedule?.let { schedule ->
            val dates = schedule.split(" - ")
            startDate = dateFormatter.parse(dates[0] + " 00:00") ?: Date()
            endDate = dateFormatter.parse(dates[1] + " 00:00") ?: Date()
        }
    }

    val exploreViewModel = ExploreViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Study Session",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Session Title Input
        OutlinedTextField(
            value = sessionTitle,
            onValueChange = { sessionTitle = it },
            label = { Text("Session Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Session Description Input
        OutlinedTextField(
            value = sessionDescription,
            onValueChange = { sessionDescription = it },
            label = { Text("Session Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        if(studyGroup.meetingType == "In-Person") {
            // Online/In-person Toggle
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Is Online: ", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isOnline,
                    onCheckedChange = { isOnline = it }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Location Input (only if in-person)
        if (!isOnline) {
            DropdownMenuField(
                options = exploreViewModel.loadMeetingLocations().map { it.name },
                selectedOption = location,
                onOptionSelected = { location = it },
            )
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            // Meeting Link Input (only if online)
            OutlinedTextField(
                value = meetingLink,
                onValueChange = { meetingLink = it },
                label = { Text("Meeting Link (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Date/Time Picker
        Button(onClick = {
            showDateTimePicker(context, sessionDateTime, startDate, endDate) {
                sessionDateTime = it
            }
        }) {
            Text(
                text = sessionDateTime?.let { "Selected: ${dateFormatter.format(it)}" }
                    ?: "Pick Date & Time"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(
            onClick = {
                if (sessionTitle.isNotBlank() && sessionDescription.isNotBlank() && (isOnline || location.isNotBlank())) {
                    val selectedDateTime = sessionDateTime
                    val validSchedule = startDate.before(selectedDateTime) && endDate.after(selectedDateTime)

                    if(!validSchedule) {
                        Toast.makeText(context, "Session must be within the group schedule", Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    }

                    val newSession = StudySession(
                        sessionId = UUID.randomUUID().toString(),
                        groupId = studyGroupId,
                        sessionTitle = sessionTitle,
                        sessionDescription = sessionDescription,
                        sessionDateTime = dateFormatter.format(sessionDateTime ?: Date()),
                        location = if (isOnline) "Online" else location,
                        isOnline = isOnline
                    )
                    studyGroupViewModel.addStudySession(
                        studySession = newSession,
                        onSuccess = {
                            Toast.makeText(context, "Session added successfully", Toast.LENGTH_SHORT)
                                .show()
                            navController.popBackStack()
                        },
                        onFailure = {
                            Toast.makeText(context, "Failed to add session: $it", Toast.LENGTH_SHORT)
                                .show()
                        }
                    )
                } else {
                    Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Session")
        }
    }
}

/**
 * Displays a date and time picker with restrictions based on the group's schedule.
 *
 * @param context The application context.
 * @param initialDate The initially selected date.
 * @param scheduleStartDate The earliest allowed date.
 * @param scheduleEndDate The latest allowed date.
 * @param onDateTimeSelected Callback for the selected date and time.
 */
fun showDateTimePicker(context: Context, initialDate: Date?, scheduleStartDate: Date, scheduleEndDate: Date, onDateTimeSelected: (Date) -> Unit) {
    val calendar = Calendar.getInstance()
    initialDate?.let { calendar.time = it }

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val minDate = scheduleStartDate.time
    val maxDate = scheduleEndDate.time

    val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        val selectedDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedYear)
            set(Calendar.MONTH, selectedMonth)
            set(Calendar.DAY_OF_MONTH, selectedDay)
        }
        TimePickerDialog(context, { _, hour, minute ->
            selectedDate.set(Calendar.HOUR_OF_DAY, hour)
            selectedDate.set(Calendar.MINUTE, minute)
            onDateTimeSelected(selectedDate.time)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }, year, month, day)

    val datePicker = datePickerDialog.datePicker
    datePicker.minDate = minDate
    datePicker.maxDate = maxDate

    datePickerDialog.show()
}
