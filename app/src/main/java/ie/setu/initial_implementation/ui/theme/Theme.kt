@file:Suppress("DEPRECATION")

package ie.setu.initial_implementation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = Background,
    surface = Surface,
    error = Error,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary,
    onBackground = OnBackground,
    onSurface = OnSurface,
    onError = OnError
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    error = ErrorDark,
    onPrimary = OnPrimaryDark,
    onSecondary = OnSecondaryDark,
    onBackground = OnBackgroundDark,
    onSurface = OnSurfaceDark,
    onError = OnErrorDark
)

@Composable
fun InitialImplementationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color disabled by default for elderly users
    // since consistent color schemes is preferred
    dynamicColor: Boolean = false,
    highContrast: Boolean = false, // Parameter for high contrast mode
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Apply high contrast adjustments if needed
    val finalColorScheme = if (highContrast) {
        if (darkTheme) {
            // Increase contrast for dark theme
            colorScheme.copy(
                background = BackgroundDark,
                surface = SurfaceDark,
                onBackground = Color(0xFFFFFFFF), // Pure white for maximum contrast
                onSurface = Color(0xFFFFFFFF)     // Pure white for maximum contrast
            )
        } else {
            // Increase contrast for light theme
            colorScheme.copy(
                background = Color(0xFFFFFFFF),   // Pure white for maximum contrast
                surface = Color(0xFFFFFFFF),      // Pure white for maximum contrast
                onBackground = Color(0xFF000000), // Pure black for maximum contrast
                onSurface = Color(0xFF000000)     // Pure black for maximum contrast
            )
        }
    } else {
        colorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = finalColorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = finalColorScheme,
        typography = Typography,
        content = content
    )
}