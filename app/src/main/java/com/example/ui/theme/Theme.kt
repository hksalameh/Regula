package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

fun getLightColorScheme(palette: AppColorPalette): ColorScheme {
    val (primary, onPrimary, container, onContainer, secondary, onSecondary, secondaryContainer, onSecondaryContainer) = when (palette) {
        AppColorPalette.NATURAL_TONES -> listOf(NaturalPrimary, NaturalOnPrimary, NaturalPrimaryContainer, NaturalOnPrimaryContainer, NaturalSecondary, NaturalOnSecondary, NaturalSecondaryContainer, NaturalOnSecondaryContainer)
        AppColorPalette.ROYAL_NAVY -> listOf(RoyalPrimary, RoyalOnPrimary, RoyalPrimaryContainer, RoyalOnPrimaryContainer, RoyalSecondary, RoyalOnSecondary, RoyalSecondaryContainer, RoyalOnSecondaryContainer)
        AppColorPalette.OCEANIC_TEAL -> listOf(OceanPrimary, OceanOnPrimary, OceanPrimaryContainer, OceanOnPrimaryContainer, OceanSecondary, OceanOnSecondary, OceanSecondaryContainer, OceanOnSecondaryContainer)
        AppColorPalette.SAPPHIRE_GOLD -> listOf(SapphirePrimary, SapphireOnPrimary, SapphirePrimaryContainer, SapphireOnPrimaryContainer, SapphireSecondary, SapphireOnSecondary, SapphireSecondaryContainer, SapphireOnSecondaryContainer)
        AppColorPalette.CLASSIC_INDIGO -> listOf(IndigoPrimary, IndigoOnPrimary, IndigoPrimaryContainer, IndigoOnPrimaryContainer, IndigoSecondary, IndigoOnSecondary, IndigoSecondaryContainer, IndigoOnSecondaryContainer)
    }
    return lightColorScheme(
        primary = primary, onPrimary = onPrimary,
        primaryContainer = container, onPrimaryContainer = onContainer,
        secondary = secondary, onSecondary = onSecondary,
        secondaryContainer = secondaryContainer, onSecondaryContainer = onSecondaryContainer,
        tertiary = secondary, onTertiary = onSecondary,
        tertiaryContainer = secondaryContainer, onTertiaryContainer = onSecondaryContainer,
        background = NaturalBackground, onBackground = NaturalOnBackground,
        surface = NaturalSurface, onSurface = NaturalOnSurface,
        surfaceVariant = NaturalSurfaceVariant, onSurfaceVariant = NaturalOnSurfaceVariant,
        outline = NaturalOutline, outlineVariant = NaturalOutline,
        surfaceContainerLowest = Color.White,
        surfaceContainerLow = NaturalBackground,
        surfaceContainer = NaturalSurfaceVariant,
        surfaceContainerHigh = NaturalPrimaryContainer,
        surfaceContainerHighest = NaturalPrimaryContainer,
        surfaceBright = Color.White, surfaceDim = Color(0xFFE7EBE6),
        inverseSurface = NaturalOnSurface, inverseOnSurface = Color.White,
        inversePrimary = Color(0xFFB8D5C8),
        error = Color(0xFFB32635), onError = Color.White,
        errorContainer = Color(0xFFF9E3E4), onErrorContainer = Color(0xFF84202B)
    )
}

fun getDarkColorScheme(palette: AppColorPalette): ColorScheme {
    val (primary, secondary) = when (palette) {
        AppColorPalette.NATURAL_TONES -> Color(0xFFB8D5C8) to Color(0xFFA8C9BC)
        AppColorPalette.ROYAL_NAVY -> Color(0xFFB5CBF3) to Color(0xFFA5C8ED)
        AppColorPalette.OCEANIC_TEAL -> Color(0xFFA5D4E6) to Color(0xFF9BD6D0)
        AppColorPalette.SAPPHIRE_GOLD -> Color(0xFFB9C9F1) to Color(0xFFE2C998)
        AppColorPalette.CLASSIC_INDIGO -> Color(0xFFB7C8E5) to Color(0xFFA8C9E5)
    }
    val darkText = Color(0xFF10201B)
    val lightText = Color(0xFFE4EDE7)
    val container = Color(0xFF28453B)
    return darkColorScheme(
        primary = primary, onPrimary = darkText,
        primaryContainer = container, onPrimaryContainer = lightText,
        secondary = secondary, onSecondary = darkText,
        secondaryContainer = Color(0xFF30483F), onSecondaryContainer = lightText,
        tertiary = secondary, onTertiary = darkText,
        tertiaryContainer = container, onTertiaryContainer = lightText,
        background = DarkBackground, onBackground = lightText,
        surface = DarkSurface, onSurface = lightText,
        surfaceVariant = DarkSurfaceVariant, onSurfaceVariant = Color(0xFFC0D0C6),
        outline = Color(0xFF71887A), outlineVariant = Color(0xFF3A5045),
        surfaceContainerLowest = Color(0xFF0C1613),
        surfaceContainerLow = Color(0xFF14221D),
        surfaceContainer = DarkSurface,
        surfaceContainerHigh = DarkSurfaceVariant,
        surfaceContainerHighest = Color(0xFF30453B),
        surfaceBright = Color(0xFF34483E), surfaceDim = DarkBackground,
        inverseSurface = lightText, inverseOnSurface = darkText,
        inversePrimary = Color(0xFF294F46),
        error = Color(0xFFFFB4AB), onError = Color(0xFF690005),
        errorContainer = Color(0xFF84202B), onErrorContainer = Color(0xFFFFDAD6)
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    palette: AppColorPalette = AppColorPalette.NATURAL_TONES,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) getDarkColorScheme(palette) else getLightColorScheme(palette),
        typography = Typography,
        content = content
    )
}
