package com.example.ui.theme

import androidx.compose.ui.graphics.Color

enum class AppColorPalette(
    val titleAr: String,
    val titleEn: String,
    val subtitleAr: String,
    val subtitleEn: String,
    val previewColors: List<Color>
) {
    NATURAL_TONES(
        titleAr = "تناغم طبيعي هادئ (Natural Tones)",
        titleEn = "Natural Tones (Organic Sage & Ivory)",
        subtitleAr = "عاجي دافئ #FAF9F6 + زيتوني ترابي #3A4A2D + سيج مريح #E1E9D6",
        subtitleEn = "Warm ivory canvas + earthen olive + soft soothing sage",
        previewColors = listOf(Color(0xFF3A4A2D), Color(0xFFE1E9D6), Color(0xFFF1F1E8), Color(0xFFFAF9F6))
    ),
    ROYAL_NAVY(
        titleAr = "تدرج كحلي مخملي فاخر (Indigo Velvet)",
        titleEn = "Indigo Velvet & Snow Mist",
        subtitleAr = "تدرج كحلي ملكي عميق #0F2B5C مع أبيض ثلجي مريح ولمسات سماوية",
        subtitleEn = "Deep velvet navy + comfortable snow mist white + sky accent",
        previewColors = listOf(Color(0xFF0F2B5C), Color(0xFF1E4287), Color(0xFF0284C7), Color(0xFFF8FAFC))
    ),
    OCEANIC_TEAL(
        titleAr = "أزرق محيطي داكن وبورسلان سريري",
        titleEn = "Clinical Porcelain & Oceanic Navy",
        subtitleAr = "أزرق محيطي هادئ #0C3E5E + بورسلان ناعم + تركواز شفائي",
        subtitleEn = "Deep oceanic navy + soft porcelain tone + restorative teal",
        previewColors = listOf(Color(0xFF0C3E5E), Color(0xFF14537D), Color(0xFF0D9488), Color(0xFFF1F5F9))
    ),
    SAPPHIRE_GOLD(
        titleAr = "أزرق ياقوتي ملكي مع لمسة كهرمانية",
        titleEn = "Sapphire Blue & Warm Amber",
        subtitleAr = "أزرق داكن راقٍ + أبيض نقي + لمسة كهرمانية دافئة منسجمة",
        subtitleEn = "Deep sapphire + pure white + harmonious warm gold accent",
        previewColors = listOf(Color(0xFF132247), Color(0xFF1E3A8A), Color(0xFFD97706), Color(0xFFF8FAFC))
    ),
    CLASSIC_INDIGO(
        titleAr = "الوضع المدمج الفاخر (Modern Deep Blue)",
        titleEn = "Modern Deep Blue Canvas",
        subtitleAr = "خلفية كحلية عميقة ومريحة جداً للعين #0B192C مع خطوط بيضاء ناعمة",
        subtitleEn = "Luxurious deep navy canvas reducing eye strain + crisp soft white",
        previewColors = listOf(Color(0xFF0B192C), Color(0xFF1E3E62), Color(0xFF38BDF8), Color(0xFFF1F5F9))
    )
}

// 0. Natural Tones (Requested Design)
val NaturalBackground = Color(0xFFFAF9F6)
val NaturalOnBackground = Color(0xFF1C1C17)
val NaturalHeader = Color(0xFF43493E)
val NaturalPrimary = Color(0xFF3A4A2D)
val NaturalOnPrimary = Color(0xFFFFFFFF)
val NaturalPrimaryContainer = Color(0xFFE1E9D6)
val NaturalOnPrimaryContainer = Color(0xFF1C1C17)
val NaturalSecondary = Color(0xFF43493E)
val NaturalOnSecondary = Color(0xFFFFFFFF)
val NaturalSecondaryContainer = Color(0xFFE3E4D7)
val NaturalOnSecondaryContainer = Color(0xFF3A4A2D)
val NaturalSurface = Color(0xFFFFFFFF)
val NaturalOnSurface = Color(0xFF1C1C17)
val NaturalSurfaceVariant = Color(0xFFF1F1E8)
val NaturalOnSurfaceVariant = Color(0xFF43493E)
val NaturalOutline = Color(0xFFE3E4D7)
val NaturalBadge = Color(0xFFD1DBC1)

// 1. Royal Navy (Deep Indigo Velvet - scientifically calibrated for maximum harmony and zero glare)
val RoyalPrimary = Color(0xFF0F2B5C)
val RoyalOnPrimary = Color(0xFFFFFFFF)
val RoyalPrimaryContainer = Color(0xFF1E4287)
val RoyalOnPrimaryContainer = Color(0xFFFFFFFF)
val RoyalSecondary = Color(0xFF0284C7)
val RoyalOnSecondary = Color(0xFFFFFFFF)
val RoyalSecondaryContainer = Color(0xFFE0F2FE)
val RoyalOnSecondaryContainer = Color(0xFF0369A1)
val RoyalDarkAccent = Color(0xFF091B3A)

// 2. Oceanic Teal (Deep Oceanic Blue & Clinical Porcelain)
val OceanPrimary = Color(0xFF0C3E5E)
val OceanOnPrimary = Color(0xFFFFFFFF)
val OceanPrimaryContainer = Color(0xFF14537D)
val OceanOnPrimaryContainer = Color(0xFFFFFFFF)
val OceanSecondary = Color(0xFF0D9488)
val OceanOnSecondary = Color(0xFFFFFFFF)
val OceanSecondaryContainer = Color(0xFFCCFBF1)
val OceanOnSecondaryContainer = Color(0xFF115E59)

// 3. Sapphire Gold (Deep Midnight Sapphire)
val SapphirePrimary = Color(0xFF132247)
val SapphireOnPrimary = Color(0xFFFFFFFF)
val SapphirePrimaryContainer = Color(0xFF1D356D)
val SapphireOnPrimaryContainer = Color(0xFFFFFFFF)
val SapphireSecondary = Color(0xFFD97706)
val SapphireOnSecondary = Color(0xFFFFFFFF)
val SapphireSecondaryContainer = Color(0xFFFEF3C7)
val SapphireOnSecondaryContainer = Color(0xFF92400E)

// 4. Classic Indigo (Modern Deep Blue Theme)
val IndigoPrimary = Color(0xFF0B192C)
val IndigoOnPrimary = Color(0xFFFFFFFF)
val IndigoPrimaryContainer = Color(0xFF1E3E62)
val IndigoOnPrimaryContainer = Color(0xFFFFFFFF)
val IndigoSecondary = Color(0xFF38BDF8)
val IndigoOnSecondary = Color(0xFF0B192C)
val IndigoSecondaryContainer = Color(0xFFE0F2FE)
val IndigoOnSecondaryContainer = Color(0xFF0369A1)

// Pure and Minimal Surfaces with tinted comfort canvas (Soft Porcelain & Snow Mist)
val PureWhiteSurface = Color(0xFFFFFFFF)
val SoftPorcelainSurface = Color(0xFFF8FAFC)
val LightCleanBackground = Color(0xFFF1F5F9)
val LightSubtleBorder = Color(0xFFCBD5E1)
val DeepDarkText = Color(0xFF0F172A)
val MutedSlateText = Color(0xFF475569)

// Dark Theme Variants
val DarkBackground = Color(0xFF0A1120)
val DarkSurface = Color(0xFF0F1B2E)
val DarkSurfaceVariant = Color(0xFF1A2B47)

// Bristol Scale & Medical Status Colors
val BristolConstipationSevere = Color(0xFFB45309)
val BristolConstipationMild = Color(0xFFC26D38)
val BristolNormalSlightConstip = Color(0xFF64748B)
val BristolIdeal = Color(0xFF059669)
val BristolLoose = Color(0xFFD97706)
val BristolDiarrheaLight = Color(0xFFE11D48)
val BristolDiarrhea = Color(0xFFDC2626)
