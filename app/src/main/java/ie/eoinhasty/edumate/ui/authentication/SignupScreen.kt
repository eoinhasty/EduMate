package ie.eoinhasty.edumate.ui.authentication

/**
 * SignUpScreen.kt
 * This file contains the implementation of the Sign-Up screen, allowing users to register an account.
 *
 * Features:
 * - Collects user details such as name, email, password, and academic year.
 * - Performs user registration through a provided ViewModel.
 * - Includes input validation and dynamic UI elements for better UX.
 *
 * Dependencies:
 * - ViewModel for managing sign-up logic.
 * - Navigation Controller for navigating between screens.
 * - Material 3 components for modern UI design.
 *
 * Author: [Eoin Hasty]
 * Date: [16/12/2024]
 */

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ie.eoinhasty.edumate.ui.studygroups.DropdownMenuField
import ie.eoinhasty.edumate.ui.theme.EduMateTheme

/**
 * Composable function to display the Sign-Up screen.
 *
 * @param navController Navigation controller for navigation between screens.
 * @param viewModel ViewModel responsible for sign-up logic.
 */
@Composable
fun SignUpScreen(
    navController: NavHostController,
    viewModel: SignupViewModel
) {
    val context = LocalContext.current

    // State variables for user input
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    val signupState by viewModel.signupState.collectAsState()

    val years = listOf("SD1", "SD2", "SD3", "SD4")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // EduMate Title
            Text(
                text = "EduMate",
                style = MaterialTheme.typography.displayMedium.copy(fontSize = 64.sp),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Input fields for user details
            TextFieldWithLabel(
                label = "First Name",
                value = firstName,
                onValueChange = { firstName = it }
            )

            TextFieldWithLabel(
                label = "Last Name",
                value = lastName,
                onValueChange = { lastName = it }
            )

            TextFieldWithLabel(
                label = "Email",
                value = email,
                onValueChange = { email = it }
            )

            TextFieldWithLabel(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                isPassword = true
            )

            DropdownMenuField(
                options = years,
                selectedOption = year,
                onOptionSelected = { year = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sign-Up Button
            Button(
                onClick = {
                    viewModel.signUp(
                        email = email,
                        password = password,
                        firstName = firstName,
                        lastName = lastName,
                        year = year,
                        onSuccess = {
                            navController.navigate("login")
                        },
                        onFailure = {
                            val message = it
                            Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Sign up",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
            }

            // Loading, success, or error state handling
            when(signupState) {
                is SignupState.Loading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is SignupState.Success -> {
                    Toast.makeText(context, "Sign up successful", Toast.LENGTH_SHORT).show()
                }
                is SignupState.Error -> {
                    val message = (signupState as SignupState.Error).message
                    Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cancel Button
            OutlinedButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                )
            }
        }
    }
}

/**
 * Composable helper function to create labeled text fields.
 *
 * @param label The label for the text field.
 * @param value Current value of the text field.
 * @param onValueChange Callback for text change events.
 * @param isPassword Flag indicating if the field is for passwords.
 */
@Composable
fun TextFieldWithLabel(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    Column {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Start),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            ),
            label = {
                Text(
                    text = "Enter $label",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            ),
            visualTransformation = if(isPassword) PasswordVisualTransformation() else VisualTransformation.None
        )
    }
}

@Preview
@Composable
fun SignUpScreenDarkPreview() {
    val navController = rememberNavController()
    val viewModel = SignupViewModel()

    EduMateTheme(darkTheme = true, dynamicColor = false) {
        SignUpScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Preview
@Composable
fun SignUpScreenLightPreview() {
    val navController = rememberNavController()
    val viewModel = SignupViewModel()

    EduMateTheme(darkTheme = false, dynamicColor = false) {
        SignUpScreen(navController, viewModel)
    }
}
