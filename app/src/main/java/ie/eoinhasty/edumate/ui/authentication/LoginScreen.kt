package ie.eoinhasty.edumate.ui.authentication

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import ie.eoinhasty.edumate.data.database.UserRepository
import ie.eoinhasty.edumate.data.users.UserDao
import ie.eoinhasty.edumate.ui.navigation.Screen
import ie.eoinhasty.edumate.ui.theme.EduMateTheme
import kotlin.math.log

/**
 * LoginScreen.kt
 * This file contains the implementation of the LoginScreen Composable, which provides the user interface
 * for logging into the EduMate application.
 *
 * Features:
 * - Email and password input fields for user credentials.
 * - Login functionality with success and error handling.
 * - Navigation to the registration screen for new users.
 *
 * Dependencies:
 * - Firebase for user authentication.
 * - ViewModel for managing login state.
 *
 * Author: [Eoin Hasty]
 * Date: [16/12/2024]
 */

/**
 * Composable function for rendering the LoginScreen UI.
 *
 * @param navController The NavHostController to handle navigation.
 * @param viewModel The LoginViewModel for managing login state.
 * @param onLoginSuccess A callback triggered when the login is successful.
 */
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsState()

    // Navigate to MyStudyGroupsScreen when login is successful
    LaunchedEffect(loginState) {
        if(loginState is LoginState.Success) {
            onLoginSuccess()
            viewModel.resetState()
            Log.d("LoginScreen", "Navigating to MyStudyGroupsScreen")
        }
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // LoginScreen UI
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

            Spacer(modifier = Modifier.height(48.dp))

            // Email Input
            Text(
                text = "Email:",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                ),
                label = {
                    Text(
                        text = "Enter Email",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Password Input
            Text(
                text = "Password:",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Start)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                ),
                label = {
                    Text(
                        text = "Enter Password",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                ),
                visualTransformation = PasswordVisualTransformation(),
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Login Button
            Button(
                onClick = {
                    viewModel.login(
                        email = email,
                        password = password,
                        onSuccess = {
                            onLoginSuccess()
                            viewModel.resetState()
                        },
                        onFailure = { message ->
                            viewModel.resetState()
                            Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Login",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Loading Indicator and Error Handling
            when (loginState) {
                is LoginState.Loading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is LoginState.Error -> {
                    val message = (loginState as LoginState.Error).message
                    Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }

            // Sign-Up Button
            OutlinedButton(
                onClick = {
                    navController.navigate(Screen.Register.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Sign up",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                )
            }
        }
    }
}