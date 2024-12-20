package ie.eoinhasty.edumate.ui.studygroups

/**
 * AddStudyGroupScreen.kt
 * Allows users to create a new study group by specifying group details.
 *
 * Features:
 * - Displays a form for creating a new study group.
 * - Allows users to specify group name, description, meeting type, year, category, schedule, and max members.
 * - Validates user input and displays error messages.
 * - Navigates to the Study Group screen after creating a group.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import android.content.Context
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ie.eoinhasty.edumate.data.studygroups.StudyGroup
import ie.eoinhasty.edumate.ui.BaseContainer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.math.max

/**
 * Entry point for the Add Study Group screen, allowing users to create a new study group
 * by specifying attributes such as name, description, meeting type, year, category, schedule,
 * and maximum number of members.
 *
 * @param viewModel The ViewModel managing study group data.
 * @param navController The NavHostController to handle navigation.
 */
@Composable
fun AddStudyGroupScreen(
    viewModel: StudyGroupViewModel,
    navController: NavHostController
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
            AddStudyGroupScreenContent(viewModel, navController)
        }
    }
}

/**
 * Displays the UI components for creating a new study group.
 *
 * @param viewModel The ViewModel managing the logic and data for study groups.
 * @param navController The NavHostController to navigate after creating a group.
 */
@Composable
fun AddStudyGroupScreenContent(
    viewModel: StudyGroupViewModel,
    navController: NavHostController
) {
    var groupName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var meetingType by remember { mutableStateOf("In-Person") }
    var year by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var icon by remember { mutableStateOf("") }
    var maxMembers by remember { mutableStateOf(10) }

    var scheduleStartDate by remember { mutableStateOf<Date?>(null) }
    var scheduleEndDate by remember { mutableStateOf<Date?>(null) }

    val meetingTypes = listOf("In-Person", "Online")
    val years = listOf("SD1", "SD2", "SD3", "SD4")
    val categories = listOf("Project Work", "Exam Prep", "General Study")
    val icons = viewModel.iconMap.keys.toList()

    val context = LocalContext.current
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Group Name
                Text("Group Name:")
                OutlinedTextField(
                    value = groupName,
                    onValueChange = { groupName = it },
                    label = { Text("Group Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                // Icon
                Text("Icon:")
                DropdownMenuField(
                    options = icons,
                    selectedOption = icon,
                    onOptionSelected = { icon = it }
                )
            }

            item {
                // Meeting Type
                Text("Type of Meeting:")
                DropdownMenuField(
                    options = meetingTypes,
                    selectedOption = meetingType,
                    onOptionSelected = { meetingType = it }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                // Year
                Text("Year:")
                DropdownMenuField(
                    options = years,
                    selectedOption = year,
                    onOptionSelected = { year = it }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                // Category
                Text("Category:")
                DropdownMenuField(
                    options = categories,
                    selectedOption = category,
                    onOptionSelected = { category = it }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Schedule date pickers
            item {
                Text("Schedule:")
                Button(onClick = {
                    showDatePickerDialog(context, scheduleStartDate) {
                        scheduleStartDate = it
                    }
                }) {
                    Text(
                        text = scheduleStartDate?.let { "Start Date: ${dateFormatter.format(it)}" }
                            ?: "Pick Start Date"
                    )
                }

                Button(onClick = {
                    showDatePickerDialog(context, scheduleStartDate) {
                        scheduleEndDate = it
                    }
                }
                ) {
                    Text(
                        text = scheduleEndDate?.let { "End Date: ${dateFormatter.format(it)}" }
                            ?: "Pick End Date"
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                //Max Members
                OutlinedTextField(
                    value = maxMembers.toString(),
                    onValueChange = { maxMembers = it.toIntOrNull() ?: 10 },
                    label = { Text("Max Members") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Submit Button
                Button(
                    onClick = {
                        if (groupName.isNotBlank() && description.isNotBlank() && year.isNotBlank() && category.isNotBlank()) {
                            loading = true
                            val schedule = "${dateFormatter.format(scheduleStartDate)} - ${dateFormatter.format(scheduleEndDate)}"

                            val newGroup = StudyGroup(
                                groupId = UUID.randomUUID().toString(),
                                groupName = groupName,
                                description = description,
                                meetingType = meetingType,
                                year = year,
                                schedule = schedule,
                                category = category,
                                iconName = icon,
                                members = emptyList(),
                                memberCount = 0,
                                maxMembers = max(1, maxMembers),
                                createdBy = viewModel.currentUser.value?.userId ?: "",
                            )

                            viewModel.addStudyGroup(
                                studyGroup = newGroup,
                                onSuccess = {
                                    loading = false
                                    navController.popBackStack()
                                },
                                onFailure = {
                                    loading = false
                                    Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
                                }
                            )
                        } else {
                            Toast.makeText(
                                context,
                                "Please fill out all fields",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create Group")
                }

                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
    }
}

/**
 * Displays a dropdown menu for selecting an option.
 *
 * @param options List of selectable options.
 * @param selectedOption The currently selected option.
 * @param onOptionSelected Callback triggered when an option is selected.
 */
@Composable
fun DropdownMenuField(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Option") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Displays a date picker dialog.
 *
 * @param context The current context.
 * @param initialDate The initial date to display in the picker.
 * @param onDateSelected Callback triggered when a date is selected.
 */
fun showDatePickerDialog(
    context: Context,
    initialDate: Date?,
    onDateSelected: (Date) -> Unit
) {
    val calendar = Calendar.getInstance()
    initialDate?.let { calendar.time = it }

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        val selectedCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedYear)
            set(Calendar.MONTH, selectedMonth)
            set(Calendar.DAY_OF_MONTH, selectedDay)
        }
        onDateSelected(selectedCalendar.time)
    }, year, month, day).show()
}

