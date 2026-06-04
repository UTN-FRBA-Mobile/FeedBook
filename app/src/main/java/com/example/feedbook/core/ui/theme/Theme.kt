package com.example.feedbook.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary      = FeedNavy,
    secondary    = FeedBrown,
    tertiary     = FeedCream,
    surface      = FeedSurface,
    onPrimary    = Color.White,
    onSecondary  = Color.White,
    onSurface    = FeedNeutral,
    inverseSurface = FeedNavy,
    inverseOnSurface = Color.White,
    error        = FeedError
)

private val DarkColorScheme = darkColorScheme(
    primary      = FeedCream,
    secondary    = FeedBrown,
    tertiary     = FeedNavy,
    surface      = FeedNeutral,
    onPrimary    = FeedNeutral,
    onSurface    = FeedCream,
    inverseSurface = FeedNeutral,
    inverseOnSurface = Color.White,
    error        = FeedError
)

@Composable
fun FeedBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
