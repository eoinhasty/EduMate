package ie.eoinhasty.edumate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ie.eoinhasty.edumate.ui.authentication.LoginScreen
import ie.eoinhasty.edumate.ui.theme.EduMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduMateTheme(dynamicColor = false) {
                LoginScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EduMateTheme(dynamicColor = false) {
        LoginScreen()
    }
}