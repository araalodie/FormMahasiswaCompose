package com.example.formmahasiswa.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryIndigo,
    onPrimary = OnPrimaryIndigo,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryTeal,
    onSecondary = OnSecondaryTeal,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    error = DarkError,
    onError = OnDarkError
)

@Composable
fun FormMahasiswaTheme(
    content: @Composable () -> Unit
) {
    // Dipaksa menggunakan tema gelap sesuai spesifikasi
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
