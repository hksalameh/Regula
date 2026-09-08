package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppColorPalette

data class ProposedPaletteSpec(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val tag: String,
    val ruleFormula: String,
    val descriptionAr: String,
    val descriptionEn: String,
    val gradientStart: Color,
    val gradientEnd: Color,
    val deepBlue: Color,
    val deepBlueHex: String,
    val pureWhite: Color,
    val pureWhiteHex: String,
    val secondaryHarmonious: Color,
    val secondaryHarmoniousNameAr: String,
    val secondaryHarmoniousNameEn: String,
    val secondaryHarmoniousHex: String,
    val surfaceColor: Color,
    val containerBg: Color,
    val isRecommended: Boolean = false,
    val mappedPalette: AppColorPalette
)

@Composable
fun ColorPalettePreviewScreen(
    currentPalette: AppColorPalette,
    appLanguage: String,
    onSelectAndApply: (AppColorPalette) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val isAr = appLanguage == "AR"
    val scrollState = rememberScrollState()

    // Interactive Harmonizer Sliders:
    // 1. Contrast Warmth (from crisp clinical white 0.0 to warm soft porcelain 1.0)
    var surfaceWarmth by remember { mutableFloatStateOf(0.45f) }
    // 2. Depth of primary card (solid deep blue vs rich velvet gradient)
    var useVelvetGradient by remember { mutableStateOf(true) }

    // 3 Scientific & Harmonious Concept Palettes
    val proposedPalettes = remember {
        listOf(
            ProposedPaletteSpec(
                id = "indigo_velvet",
                nameAr = "التدرج المخملي الفاخر (Indigo Velvet)",
                nameEn = "Indigo Velvet & Snow Mist",
                tag = if (isAr) "الخيار الأرقى والأكثر راحة للعين (موصى به عالمياً)" else "Global Gold Standard (Most Recommended)",
                ruleFormula = "30% Deep Navy Gradient • 60% Snow Mist Surface • 10% Sky Blue",
                descriptionAr = "يعالج مشكلة 'صدمة التباين' عبر تدرج كحلي ملكي مخملي هادئ (#0F2B5C إلى #1A3B73)، مع أبيض ثلجي مطفأ مريح للعين (#F8FAFC)، ولمسات أزرق سماوي كريستالي (#0284C7).",
                descriptionEn = "Solves harsh contrast using smooth royal velvet navy gradient, calming snow mist surface, and sky blue accents.",
                gradientStart = Color(0xFF0F2B5C),
                gradientEnd = Color(0xFF1E4287),
                deepBlue = Color(0xFF0F2B5C),
                deepBlueHex = "#0F2B5C",
                pureWhite = Color(0xFFFFFFFF),
                pureWhiteHex = "#FFFFFF",
                secondaryHarmonious = Color(0xFF0284C7),
                secondaryHarmoniousNameAr = "أزرق سماوي هادئ (Sky Blue)",
                secondaryHarmoniousNameEn = "Soft Sky Blue",
                secondaryHarmoniousHex = "#0284C7",
                surfaceColor = Color(0xFFF8FAFC),
                containerBg = Color(0xFF163260),
                isRecommended = true,
                mappedPalette = AppColorPalette.ROYAL_NAVY
            ),
            ProposedPaletteSpec(
                id = "clinical_porcelain",
                nameAr = "البورسلان السريري والكحلي المحيطي (Clinical Porcelain)",
                nameEn = "Clinical Porcelain & Oceanic Navy",
                tag = if (isAr) "خيار العيادات الطبية فائقة النظافة" else "Ultra-Clean Clinical Aesthetic",
                ruleFormula = "60% Warm Porcelain • 30% Oceanic Navy • 10% Therapeutic Teal",
                descriptionAr = "خلفية ناعمة جداً بدرجة البورسلان الدافئ المهدئ للأعصاب، مع بطاقات ناصعة التباين وكتابات كحلية واضحة جداً، ولمسات تركوازية مهدئة لراحة الجهاز الهضمي.",
                descriptionEn = "Calming clinical porcelain surface with authoritative oceanic navy typography and digestive therapeutic teal accents.",
                gradientStart = Color(0xFF0C3E5E),
                gradientEnd = Color(0xFF14537D),
                deepBlue = Color(0xFF0C3E5E),
                deepBlueHex = "#0C3E5E",
                pureWhite = Color(0xFFFFFFFF),
                pureWhiteHex = "#FFFFFF",
                secondaryHarmonious = Color(0xFF0D9488),
                secondaryHarmoniousNameAr = "تركوازي طبي شفائي (Medical Teal)",
                secondaryHarmoniousNameEn = "Restorative Teal",
                secondaryHarmoniousHex = "#0D9488",
                surfaceColor = Color(0xFFF1F5F9),
                containerBg = Color(0xFF0E334D),
                isRecommended = false,
                mappedPalette = AppColorPalette.OCEANIC_TEAL
            ),
            ProposedPaletteSpec(
                id = "modern_deep_navy",
                nameAr = "الوضع المدمج الفاخر (Modern Deep Blue Canvas)",
                nameEn = "Modern Deep Blue Canvas",
                tag = if (isAr) "طراز التطبيقات الصحية الحديثة (Whoop / Oura)" else "Modern Health App Style",
                ruleFormula = "70% Deep Slate Canvas • 20% Crisp White Glyphs • 10% Ice Blue",
                descriptionAr = "يلغي الخلفية البيضاء تماماً! خلفية التطبيق كحلية داكنة راقية وفخمة (#0B192C)، والنصوص بيضاء ناصعة ذات مقروئية فورية، مع مؤشرات باللون الأزرق الثلجي.",
                descriptionEn = "Eliminates all white glare completely with a serene midnight navy canvas, crisp white glyphs, and ice blue indicators.",
                gradientStart = Color(0xFF0B192C),
                gradientEnd = Color(0xFF1E3E62),
                deepBlue = Color(0xFF0B192C),
                deepBlueHex = "#0B192C",
                pureWhite = Color(0xFFFFFFFF),
                pureWhiteHex = "#FFFFFF",
                secondaryHarmonious = Color(0xFF38BDF8),
                secondaryHarmoniousNameAr = "أزرق جليدي ثلجي (Ice Blue)",
                secondaryHarmoniousNameEn = "Ice Sky Blue",
                secondaryHarmoniousHex = "#38BDF8",
                surfaceColor = Color(0xFF1E293B),
                containerBg = Color(0xFF17283C),
                isRecommended = false,
                mappedPalette = AppColorPalette.CLASSIC_INDIGO
            )
        )
    }

    var selectedPaletteSpec by remember {
        mutableStateOf(
            proposedPalettes.firstOrNull { it.mappedPalette == currentPalette } ?: proposedPalettes[0]
        )
    }

    // Dynamic interpolated card background based on user's slider
    val dynamicCardColor = remember(selectedPaletteSpec, surfaceWarmth) {
        if (selectedPaletteSpec.id == "modern_deep_navy") {
            Color(0xFF0B192C)
        } else {
            // Smoothly blend between pure white (#FFFFFF) and soft cream porcelain (#F1F5F9)
            Color(
                red = 1.0f - (surfaceWarmth * 0.05f),
                green = 1.0f - (surfaceWarmth * 0.04f),
                blue = 1.0f - (surfaceWarmth * 0.02f)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .testTag("color_palette_preview_screen")
    ) {
        // --- 1. TOP BAR WITH BACK BUTTON ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("palette_preview_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = if (isAr) "رجوع" else "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = if (isAr) "استوديو التناغم اللوني الذكي" else "Color Harmonizer Studio",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isAr) "حل علمي للتناسق بين الأزرق العميق والأبيض" else "Scientific Harmony for Deep Blue & White",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = selectedPaletteSpec.deepBlue
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isAr) "تناسق معتمد" else "Calibrated",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // --- 2. LIVE SHOWCASE HERO CARD WITH VELVET GRADIENT & PORCELAIN CONTRAST ---
        val heroModifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .then(
                if (useVelvetGradient) {
                    Modifier.background(
                        Brush.linearGradient(
                            listOf(selectedPaletteSpec.gradientStart, selectedPaletteSpec.gradientEnd)
                        )
                    )
                } else {
                    Modifier.background(selectedPaletteSpec.deepBlue)
                }
            )
            .border(
                1.dp,
                selectedPaletteSpec.secondaryHarmonious.copy(alpha = 0.35f),
                RoundedCornerShape(26.dp)
            )

        Box(modifier = heroModifier) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Simulated App Brand Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = selectedPaletteSpec.pureWhite,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(
                                Icons.Default.Spa,
                                contentDescription = null,
                                tint = selectedPaletteSpec.deepBlue,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isAr) "ريغولا (Regula)" else "Regula",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = selectedPaletteSpec.pureWhite
                            )
                            Text(
                                text = if (isAr) "محاكاة التناسق المباشر" else "Live Harmony Simulation",
                                fontSize = 11.5.sp,
                                color = selectedPaletteSpec.pureWhite.copy(alpha = 0.8f)
                            )
                        }
                    }

                    // Status Pill with smooth border
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = selectedPaletteSpec.pureWhite.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, selectedPaletteSpec.pureWhite.copy(alpha = 0.5f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF34D399))
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (isAr) "إيقاع منتظم" else "Regular",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = selectedPaletteSpec.pureWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Inner Simulated Metric Surface (Demonstrating the adjusted White/Porcelain card)
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = selectedPaletteSpec.containerBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, selectedPaletteSpec.pureWhite.copy(alpha = 0.18f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (isAr) "آخر عملية إخراج (مقياس بريستول)" else "Last Bowel Movement",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = selectedPaletteSpec.secondaryHarmonious
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isAr) "منذ 4 ساعات • مقياس 4 (قوام مثالي)" else "4 hours ago • Type 4 (Ideal)",
                            fontSize = 17.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = selectedPaletteSpec.pureWhite
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Simulated Hydration Progress Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isAr) "الهدف اليومي للسوائل (1750 / 2000 مل)" else "Daily Fluid Goal (1750 / 2000 ml)",
                                fontSize = 11.5.sp,
                                color = selectedPaletteSpec.pureWhite.copy(alpha = 0.85f)
                            )
                            Text(
                                text = "88%",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = selectedPaletteSpec.pureWhite
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { 0.88f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = selectedPaletteSpec.secondaryHarmonious,
                            trackColor = selectedPaletteSpec.pureWhite.copy(alpha = 0.25f),
                            strokeCap = StrokeCap.Round
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons demonstrating 3 distinct harmonious roles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 1. Crisp White Button with Deep Blue Text (High Contrast Primary)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = dynamicCardColor,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Wc,
                                contentDescription = null,
                                tint = selectedPaletteSpec.deepBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAr) "تسجيل إخراج" else "Log Bowel",
                                color = selectedPaletteSpec.deepBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // 2. Translucent Border Button (Secondary Action)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.Transparent,
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, selectedPaletteSpec.pureWhite),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Medication,
                                contentDescription = null,
                                tint = selectedPaletteSpec.pureWhite,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAr) "أدوية وملينات" else "Meds",
                                color = selectedPaletteSpec.pureWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // 3. Secondary Complementary Accent Button
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = selectedPaletteSpec.secondaryHarmonious,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.LocalDrink,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isAr) "+250 مل ماء" else "+250ml",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 3. LIVE HARMONIZER TUNER (THE INTERACTIVE SOLUTION) ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Tune,
                            contentDescription = null,
                            tint = selectedPaletteSpec.deepBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAr) "أداة ضبط التوهج وتخفيف بياض الشاشة" else "White Glow & Softness Tuner",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.clickable { useVelvetGradient = !useVelvetGradient }
                    ) {
                        Text(
                            text = if (useVelvetGradient)
                                (if (isAr) "تدرج مخملي ✓" else "Velvet Gradient ✓")
                            else
                                (if (isAr) "لون مصمت" else "Solid Color"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isAr)
                        "حرك الشريط للتحكم في دفء الأبيض (من أبيض ناصع إلى بورسلان سريري ناعم لا يتعب العين):"
                    else
                        "Adjust white softness (from pure clinical white to soft eye-friendly porcelain):",
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.LightMode,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(
                        value = surfaceWarmth,
                        onValueChange = { surfaceWarmth = it },
                        valueRange = 0f..1f,
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = selectedPaletteSpec.deepBlue,
                            activeTrackColor = selectedPaletteSpec.deepBlue,
                            inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.Nightlight,
                        contentDescription = null,
                        tint = selectedPaletteSpec.deepBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isAr) "أبيض ناصع (Bright White)" else "Bright White",
                        fontSize = 10.5.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = if (isAr) "بورسلان ناعم (Soft Porcelain)" else "Soft Porcelain",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = selectedPaletteSpec.deepBlue
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 4. PALETTE HARMONY CHOICES (3 SCIENTIFIC CONCEPTS) ---
        Text(
            text = if (isAr) "اختر تركيبة التناغم العلمي الأنسب لعينك:" else "Select your preferred scientific harmony:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            proposedPalettes.forEach { paletteSpec ->
                val isSelected = selectedPaletteSpec.id == paletteSpec.id
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f) else MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) paletteSpec.deepBlue else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPaletteSpec = paletteSpec }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Color banner preview strip
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            // Deep Blue gradient (50%)
                            Box(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(paletteSpec.gradientStart, paletteSpec.gradientEnd)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isAr) "كحلي عميق 30%" else "Navy 30%",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            // Soft Surface (30%)
                            Box(
                                modifier = Modifier
                                    .weight(0.3f)
                                    .fillMaxSize()
                                    .background(paletteSpec.surfaceColor)
                                    .border(0.5.dp, Color.LightGray.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isAr) "سطح مريح 60%" else "Surface 60%",
                                    color = paletteSpec.deepBlue,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            // Complementary Accent (20%)
                            Box(
                                modifier = Modifier
                                    .weight(0.2f)
                                    .fillMaxSize()
                                    .background(paletteSpec.secondaryHarmonious),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isAr) "10% مكمل" else "Accent",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isAr) paletteSpec.nameAr else paletteSpec.nameEn,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) paletteSpec.deepBlue else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = paletteSpec.tag,
                                    fontSize = 11.sp,
                                    color = if (paletteSpec.isRecommended) Color(0xFF059669) else MaterialTheme.colorScheme.outline,
                                    fontWeight = if (paletteSpec.isRecommended) FontWeight.Bold else FontWeight.Normal
                                )
                            }

                            Icon(
                                imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isSelected) paletteSpec.deepBlue else MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = "📐 ${paletteSpec.ruleFormula}",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isAr) paletteSpec.descriptionAr else paletteSpec.descriptionEn,
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 5. APPROVAL & ADOPTION BUTTONS ---
        Button(
            onClick = {
                onSelectAndApply(selectedPaletteSpec.mappedPalette)
                Toast.makeText(
                    context,
                    if (isAr) "تم اعتماد لوحة: ${selectedPaletteSpec.nameAr} بنجاح! ✨" else "Palette ${selectedPaletteSpec.nameEn} applied! ✨",
                    Toast.LENGTH_LONG
                ).show()
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("apply_approved_palette_btn"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = selectedPaletteSpec.deepBlue)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isAr) "الموافقة واعتماد هذا التناسق للتطبيق كاملاً" else "Approve & Apply to Entire App",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (isAr) "العودة للشاشة السابقة" else "Back to Previous Screen",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ColorSwatchRow(
    role: String,
    description: String,
    color: Color,
    hexCode: String,
    hasBorder: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color)
                .then(
                    if (hasBorder) Modifier.border(1.dp, Color.Gray.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                    else Modifier
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = role,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                fontSize = 11.5.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Text(
                text = hexCode,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
