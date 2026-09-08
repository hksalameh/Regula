package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

fun getLightColorScheme(palette: AppColorPalette): ColorScheme {
    return when (palette) {
        AppColorPalette.NATURAL_TONES -> lightColorScheme(
            primary = NaturalPrimary,
            onPrimary = NaturalOnPrimary,
            primaryContainer = NaturalPrimaryContainer,
            onPrimaryContainer = NaturalOnPrimaryContainer,
            secondary = NaturalSecondary,
            onSecondary = NaturalOnSecondary,
            secondaryContainer = NaturalSecondaryContainer,
            onSecondaryContainer = NaturalOnSecondaryContainer,
            tertiary = NaturalBadge,
            background = NaturalBackground,
            onBackground = NaturalOnBackground,
            surface = NaturalSurface,
            onSurface = NaturalOnSurface,
            surfaceVariant = NaturalSurfaceVariant,
            onSurfaceVariant = NaturalOnSurfaceVariant,
            outline = NaturalOutline
        )
        AppColorPalette.ROYAL_NAVY -> lightColorScheme(
            primary = RoyalPrimary,
            onPrimary = RoyalOnPrimary,
            primaryContainer = RoyalPrimaryContainer,
            onPrimaryContainer = RoyalOnPrimaryContainer,
            secondary = RoyalSecondary,
            onSecondary = RoyalOnSecondary,
            secondaryContainer = RoyalSecondaryContainer,
            onSecondaryContainer = RoyalOnSecondaryContainer,
            tertiary = Color(0xFF0369A1),
            background = LightCleanBackground,
            onBackground = DeepDarkText,
            surface = SoftPorcelainSurface,
            onSurface = DeepDarkText,
            surfaceVariant = Color(0xFFE2E8F0),
            onSurfaceVariant = MutedSlateText,
            outline = LightSubtleBorder
        )
        AppColorPalette.OCEANIC_TEAL -> lightColorScheme(
            primary = OceanPrimary,
            onPrimary = OceanOnPrimary,
            primaryContainer = OceanPrimaryContainer,
            onPrimaryContainer = OceanOnPrimaryContainer,
            secondary = OceanSecondary,
            onSecondary = OceanOnSecondary,
            secondaryContainer = OceanSecondaryContainer,
            onSecondaryContainer = OceanOnSecondaryContainer,
            tertiary = Color(0xFF0F766E),
            background = LightCleanBackground,
            onBackground = DeepDarkText,
            surface = SoftPorcelainSurface,
            onSurface = DeepDarkText,
            surfaceVariant = Color(0xFFE2E8F0),
            onSurfaceVariant = MutedSlateText,
            outline = LightSubtleBorder
        )
        AppColorPalette.SAPPHIRE_GOLD -> lightColorScheme(
            primary = SapphirePrimary,
            onPrimary = SapphireOnPrimary,
            primaryContainer = SapphirePrimaryContainer,
            onPrimaryContainer = SapphireOnPrimaryContainer,
            secondary = SapphireSecondary,
            onSecondary = SapphireOnSecondary,
            secondaryContainer = SapphireSecondaryContainer,
            onSecondaryContainer = SapphireOnSecondaryContainer,
            tertiary = Color(0xFFB45309),
            background = LightCleanBackground,
            onBackground = DeepDarkText,
            surface = SoftPorcelainSurface,
            onSurface = DeepDarkText,
            surfaceVariant = Color(0xFFE2E8F0),
            onSurfaceVariant = MutedSlateText,
            outline = LightSubtleBorder
        )
        AppColorPalette.CLASSIC_INDIGO -> lightColorScheme(
            primary = IndigoPrimary,
            onPrimary = IndigoOnPrimary,
            primaryContainer = IndigoPrimaryContainer,
            onPrimaryContainer = IndigoOnPrimaryContainer,
            secondary = IndigoSecondary,
            onSecondary = IndigoOnSecondary,
            secondaryContainer = IndigoSecondaryContainer,
            onSecondaryContainer = IndigoOnSecondaryContainer,
            tertiary = Color(0xFF0284C7),
            background = Color(0xFF0F172A),
            onBackground = Color(0xFFF8FAFC),
            surface = Color(0xFF1E293B),
            onSurface = Color(0xFFF8FAFC),
            surfaceVariant = Color(0xFF334155),
            onSurfaceVariant = Color(0xFFCBD5E1),
            outline = Color(0xFF475569)
        )
    }
}

fun getDarkColorScheme(palette: AppColorPalette): ColorScheme {
    val (primary, sec) = when (palette) {
        AppColorPalette.NATURAL_TONES -> Pair(Color(0xFFB2CBA0), Color(0xFFD1DBC1))
        AppColorPalette.ROYAL_NAVY -> Pair(Color(0xFF60A5FA), Color(0xFF38BDF8))
        AppColorPalette.OCEANIC_TEAL -> Pair(Color(0xFF60A5FA), Color(0xFF2DD4BF))
        AppColorPalette.SAPPHIRE_GOLD -> Pair(Color(0xFF93C5FD), Color(0xFFFBBF24))
        AppColorPalette.CLASSIC_INDIGO -> Pair(Color(0xFF818CF8), Color(0xFFA5B4FC))
    }
    return darkColorScheme(
        primary = primary,
        secondary = sec,
        background = DarkBackground,
        surface = DarkSurface,
        surfaceVariant = DarkSurfaceVariant
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    palette: AppColorPalette = AppColorPalette.NATURAL_TONES,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) getDarkColorScheme(palette) else getLightColorScheme(palette)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
