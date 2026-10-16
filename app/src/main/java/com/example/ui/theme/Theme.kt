package com.example.ui.theme

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

private val VoicedocDarkColorScheme = darkColorScheme(
  primary = VoicedocLightBlue,
  onPrimary = VoicedocDarkNavy,
  primaryContainer = VoicedocDeepBlue,
  onPrimaryContainer = Color.White,
  secondary = VoicedocAccentCyan,
  onSecondary = VoicedocDarkNavy,
  secondaryContainer = VoicedocCardDark,
  onSecondaryContainer = VoicedocSkyBlue,
  tertiary = VoicedocPurpleAccent,
  background = VoicedocDarkNavy,
  onBackground = Color.White,
  surface = VoicedocSurfaceDark,
  onSurface = Color.White,
  surfaceVariant = VoicedocCardDark,
  onSurfaceVariant = Color(0xFFCBD5E1),
  outline = GlassBorder,
  outlineVariant = VoicedocCardBorder,
  error = VoicedocErrorRed,
  onError = Color.White
)

private val VoicedocLightColorScheme = lightColorScheme(
  primary = VoicedocDeepBlue,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFDBEAFE),
  onPrimaryContainer = VoicedocDeepBlue,
  secondary = VoicedocElectricBlue,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFEFF6FF),
  onSecondaryContainer = VoicedocDeepBlue,
  tertiary = VoicedocAccentCyan,
  background = Color(0xFFF8FAFC),
  onBackground = Color(0xFF0F172A),
  surface = Color.White,
  onSurface = Color(0xFF0F172A),
  surfaceVariant = Color(0xFFF1F5F9),
  onSurfaceVariant = Color(0xFF475569),
  outline = Color(0xFF93C5FD),
  outlineVariant = Color(0xFFE2E8F0),
  error = VoicedocErrorRed,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Default to sleek dark tech palette to showcase glowing voice orb and glassmorphic UI
  dynamicColor: Boolean = false, // Keep brand identity prominent
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> VoicedocDarkColorScheme
    else -> VoicedocLightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
