package ie.eoinhasty.edumate.ui.contact

/**
 * ContactUsScreen.kt
 * This file defines a screen where users can contact support via phone, email, or visit the website.
 *
 * Features:
 * - Provides direct access to call, email, or visit the website.
 * - Displays the TUS address.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ie.eoinhasty.edumate.ui.BaseContainer

/**
 * Main entry point for the Contact Us screen.
 *
 * @param navController The NavHostController to navigate between screens.
 */
@Composable
fun ContactUsScreen(navController: NavHostController) {
    BaseContainer(
        navController = navController
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ContactUsScreenContent()
        }
    }
}


/**
 * Content of the Contact Us screen, displaying options to contact support.
 */
@Composable
fun ContactUsScreenContent() {
    val context = LocalContext.current
    val phoneNumber = "+35361293000"
    val email = "support@edumate.com"
    val website = "https://tus.ie/"
    val address = """
        TUS Moylish Campus,
        Moylish Park,
        Limerick V94 EC5T
    """.trimIndent()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // Title
            Text(
                text = "Contact Us",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Phone Button
            Button(
                onClick = {
                    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$phoneNumber")
                    }
                    context.startActivity(dialIntent)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Call Us", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Email Button
            Button(
                onClick = {
                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                        putExtra(Intent.EXTRA_SUBJECT, "Support Request")
                    }
                    try {
                        context.startActivity(emailIntent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "No email client installed", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Email Us", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Website Button
            Button(
                onClick = {
                    val websiteIntent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(website)
                    }
                    context.startActivity(websiteIntent)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Visit Our Website", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Address Section
            Text(
                text = "Address",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = address,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
fun ContactUsScreenPreview() {
    ContactUsScreenContent()
}