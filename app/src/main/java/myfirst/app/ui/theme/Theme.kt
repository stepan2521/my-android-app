package myfirst.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

object Color {
    val Blue = androidx.compose.ui.graphics.Color(0, 145, 234, 255)
    val DarkBlue = androidx.compose.ui.graphics.Color(0, 95, 148, 255)

    val Red = androidx.compose.ui.graphics.Color(255, 0, 0, 255)
    val DarkRed = androidx.compose.ui.graphics.Color(117, 0, 0, 255)

    val Black = androidx.compose.ui.graphics.Color(0, 0, 0, 255)
    val Grey = androidx.compose.ui.graphics.Color(91, 89, 89, 255)
    val DarkGrey = androidx.compose.ui.graphics.Color(65, 65, 65, 255)
}

private val DarkColorScheme = darkColorScheme(
    primary = Color.DarkBlue,
    secondary = Color.DarkRed,
    background = Color.DarkGrey
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Blue,
    secondary = Color.Red,
    background = Color.Grey
)

@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}