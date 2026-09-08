package com.example.ui.theme

import androidx.compose.ui.graphics.Color

enum class AppColorPalette(
    val titleAr: String,
    val titleEn: String,
    val subtitleAr: String,
    val subtitleEn: String,
    val previewColors: List<Color>
) {
    NATURAL_TONES("أخضر هادئ وعاجي", "Calm Green & Ivory", "هوية هادئة للاستخدام اليومي", "A calm, everyday wellness palette", listOf(Color(0xFF294F46), Color(0xFFDFEBE5), Color(0xFFF7F8F5), Color.White)),
    ROYAL_NAVY("كحلي مخملي", "Indigo Velvet", "كحلي عميق مع خلفية فاتحة", "Deep navy with a light canvas", listOf(Color(0xFF0F2B5C), Color(0xFFDFE8F5), Color(0xFFF7F8FA), Color.White)),
    OCEANIC_TEAL("أزرق محيطي", "Oceanic Teal", "أزرق هادئ ولمسات تركواز", "Ocean blue and soft teal", listOf(Color(0xFF0C3E5E), Color(0xFFDCEDEB), Color(0xFFF5F8F8), Color.White)),
    SAPPHIRE_GOLD("ياقوتي وكهرماني", "Sapphire & Amber", "أزرق أنيق مع لمسة دافئة", "Sapphire with a warm accent", listOf(Color(0xFF132247), Color(0xFFE7EBF4), Color(0xFFF8F8FA), Color.White)),
    CLASSIC_INDIGO("كحلي داكن", "Classic Indigo", "ألوان كحلية عالية التباين", "A deep, high-contrast navy", listOf(Color(0xFF0B192C), Color(0xFF1E3E62), Color(0xFF38BDF8), Color(0xFFF1F5F9)))
}

val NaturalBackground = Color(0xFFF7F8F5)
val NaturalOnBackground = Color(0xFF253B36)
val NaturalHeader = Color(0xFF294F46)
val NaturalPrimary = Color(0xFF294F46)
val NaturalOnPrimary = Color.White
val NaturalPrimaryContainer = Color(0xFFDFEBE5)
val NaturalOnPrimaryContainer = Color(0xFF203D35)
val NaturalSecondary = Color(0xFF466A60)
val NaturalOnSecondary = Color.White
val NaturalSecondaryContainer = Color(0xFFE9F0EB)
val NaturalOnSecondaryContainer = Color(0xFF294F46)
val NaturalSurface = Color.White
val NaturalOnSurface = Color(0xFF253B36)
val NaturalSurfaceVariant = Color(0xFFF0F3EF)
val NaturalOnSurfaceVariant = Color(0xFF52665E)
val NaturalOutline = Color(0xFFDDE5DF)
val NaturalBadge = Color(0xFFBFD5C9)

val RoyalPrimary = Color(0xFF0F2B5C)
val RoyalOnPrimary = Color.White
val RoyalPrimaryContainer = Color(0xFFDFE8F5)
val RoyalOnPrimaryContainer = Color(0xFF0F2B5C)
val RoyalSecondary = Color(0xFF355D86)
val RoyalOnSecondary = Color.White
val RoyalSecondaryContainer = Color(0xFFE8EFF7)
val RoyalOnSecondaryContainer = Color(0xFF24476F)
val RoyalDarkAccent = Color(0xFF091B3A)

val OceanPrimary = Color(0xFF0C3E5E)
val OceanOnPrimary = Color.White
val OceanPrimaryContainer = Color(0xFFDCEAF0)
val OceanOnPrimaryContainer = Color(0xFF0C3E5E)
val OceanSecondary = Color(0xFF0D6B68)
val OceanOnSecondary = Color.White
val OceanSecondaryContainer = Color(0xFFDCEDEB)
val OceanOnSecondaryContainer = Color(0xFF115E59)

val SapphirePrimary = Color(0xFF132247)
val SapphireOnPrimary = Color.White
val SapphirePrimaryContainer = Color(0xFFE7EBF4)
val SapphireOnPrimaryContainer = Color(0xFF132247)
val SapphireSecondary = Color(0xFF795512)
val SapphireOnSecondary = Color.White
val SapphireSecondaryContainer = Color(0xFFF5EBD6)
val SapphireOnSecondaryContainer = Color(0xFF60430B)

val IndigoPrimary = Color(0xFF0B192C)
val IndigoOnPrimary = Color.White
val IndigoPrimaryContainer = Color(0xFFE0E7F0)
val IndigoOnPrimaryContainer = Color(0xFF0B192C)
val IndigoSecondary = Color(0xFF365D83)
val IndigoOnSecondary = Color.White
val IndigoSecondaryContainer = Color(0xFFE2ECF5)
val IndigoOnSecondaryContainer = Color(0xFF24476F)

val PureWhiteSurface = Color.White
val SoftPorcelainSurface = Color(0xFFF8FAFC)
val LightCleanBackground = Color(0xFFF5F7FA)
val LightSubtleBorder = Color(0xFFDDE5EB)
val DeepDarkText = Color(0xFF253B36)
val MutedSlateText = Color(0xFF52665E)
val DarkBackground = Color(0xFF101C19)
val DarkSurface = Color(0xFF182622)
val DarkSurfaceVariant = Color(0xFF263A33)

// Clinical status colors are independent of the decorative theme.
// Always accompany them with text, never rely on color alone.
val BristolConstipationSevere = Color(0xFF9A570C)
val BristolConstipationMild = Color(0xFF9A6225)
val BristolNormalSlightConstip = Color(0xFF52665E)
val BristolIdeal = Color(0xFF216C50)
val BristolLoose = Color(0xFF9A6225)
val BristolDiarrheaLight = Color(0xFFB13D4D)
val BristolDiarrhea = Color(0xFFB32635)
