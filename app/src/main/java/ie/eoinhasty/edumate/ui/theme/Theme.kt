package ie.eoinhasty.edumate.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TUSGold,
    secondary = RiverBlue,
    tertiary = PierPurple,

    background = TUSBlack,
    surface = Gray60,
    onPrimary = TUSWhite,
    onSecondary = TUSWhite,
    onTertiary = TUSWhite,
    onBackground = TUSWhite,
    onSurface = TUSWhite
)

private val LightColorScheme = lightColorScheme(
    primary = TUSGold,
    secondary = SkyBlue,
    tertiary = SalmonPink,

    background = TUSWhite,
    surface = Gray20,
    onPrimary = TUSBlack,
    onSecondary = TUSBlack,
    onTertiary = TUSBlack,
    onBackground = TUSBlack,
    onSurface = TUSBlack
)


@Composable
fun EduMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, content = content
    )
}