package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BowelEntry
import com.example.data.model.MealEntry
import com.example.data.model.MedicationEntry
import com.example.data.model.TimelineItem
import com.example.ui.SheetType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TodayHomeScreen(
    appName: String = "ريغولا",
    waterGoalMl: Int = 2000,
    lastSuccessfulBowel: BowelEntry?,
    bowels: List<BowelEntry>,
    medications: List<MedicationEntry>,
    meals: List<MealEntry>,
    timeline: List<TimelineItem>,
    onOpenSheet: (SheetType) -> Unit,
    onQuickWater: (Int) -> Unit,
    onNavigateToReport: () -> Unit,
    onNavigateToTimeline: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPalettePreview: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Time calculations
    val now = System.currentTimeMillis()
    val hoursSinceLastBowel = if (lastSuccessfulBowel != null) {
        ((now - lastSuccessfulBowel.timestamp) / (1000 * 60 * 60)).toInt()
    } else null

    // Today's entries
    val startOfDay = now - (now % (24 * 60 * 60 * 1000L))
    val todayBowels = bowels.filter { it.timestamp >= startOfDay }
    val todayMeds = medications.filter { it.timestamp >= startOfDay }
    val todayMeals = meals.filter { it.timestamp >= startOfDay }
    val todayWater = todayMeals.sumOf { it.waterMl }
    val waterProgress = (todayWater.toFloat() / waterGoalMl.toFloat()).coerceIn(0f, 1f)

    val todayTimeline = timeline.filter { it.timestamp >= startOfDay }.take(4)

    val dateFormat = SimpleDateFormat("EEEE، d MMMM", Locale("ar"))
    val timeFormat = SimpleDateFormat("hh:mm a", Locale("ar"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        // --- 1. HEADER (Natural Tones Design) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "مرحباً بك",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    letterSpacing = 1.sp
                )
                Text(
                    text = appName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = (-0.5).sp
                )
            }

            // Action Buttons: Medical Report & Settings Avatar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier
                        .clickable { onNavigateToReport() }
                        .testTag("top_report_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Description,
                            contentDescription = "تقرير الطبيب",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "تقرير الطبيب",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (onNavigateToPalettePreview != null) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.size(42.dp)
                    ) {
                        IconButton(
                            onClick = onNavigateToPalettePreview,
                            modifier = Modifier.testTag("home_palette_preview_button")
                        ) {
                            Icon(
                                Icons.Default.Palette,
                                contentDescription = "معاينة الألوان",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(42.dp)
                ) {
                    IconButton(
                        onClick = onNavigateToSettings,
                        modifier = Modifier.testTag("home_settings_button")
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "الإعدادات",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // --- 2. HERO STATUS CARD (Natural Tones Sage Container: rounded-[2rem] p-6) ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Top row with calendar icon in rounded-xl container + status pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.65f),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(9.dp)
                        )
                    }

                    val (pillText, pillBg, pillColor) = when {
                        hoursSinceLastBowel == null -> Triple("جاهز للتسجيل", Color.White.copy(alpha = 0.75f), MaterialTheme.colorScheme.onSurfaceVariant)
                        hoursSinceLastBowel <= 24 -> Triple("🟢 إيقاع طبيعي منتظم", Color.White.copy(alpha = 0.85f), Color(0xFF2E7D32))
                        hoursSinceLastBowel <= 48 -> Triple("🟡 مضى $hoursSinceLastBowel ساعة", Color.White.copy(alpha = 0.85f), Color(0xFFD97706))
                        else -> Triple("🟠 مضى $hoursSinceLastBowel ساعة", Color.White.copy(alpha = 0.85f), Color(0xFFDC2626))
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = pillBg
                    ) {
                        Text(
                            text = pillText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = pillColor,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Hero Headline
                Text(
                    text = "آخر عملية إخراج",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                val mainHeadline = when {
                    hoursSinceLastBowel == null -> "لم يسجل بعد"
                    hoursSinceLastBowel == 0 -> "منذ أقل من ساعة"
                    else -> "منذ $hoursSinceLastBowel ساعة"
                }
                Text(
                    text = mainHeadline,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                if (lastSuccessfulBowel != null) {
                    Text(
                        text = "مقياس بريستول: نوع ${lastSuccessfulBowel.bristolType} • الإجهاد: ${lastSuccessfulBowel.strainingLevel}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                Spacer(modifier = Modifier.height(14.dp))

                // Key Metrics Summary
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DailyMetricItem(
                        icon = Icons.Default.Wc,
                        label = "حركة الأمعاء",
                        value = if (todayBowels.isEmpty()) "0" else "${todayBowels.size}",
                        unit = "مرات",
                        modifier = Modifier.weight(1f)
                    )
                    DailyMetricItem(
                        icon = Icons.Default.Medication,
                        label = "أدوية وملينات",
                        value = if (todayMeds.isEmpty()) "0" else "${todayMeds.size}",
                        unit = "جرعة",
                        modifier = Modifier.weight(1f)
                    )
                    DailyMetricItem(
                        icon = Icons.Default.LocalDrink,
                        label = "الماء والسوائل",
                        value = "$todayWater",
                        unit = "مل",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Hydration progress bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "هدف السوائل ($waterGoalMl مل)",
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${(waterProgress * 100).toInt()}%",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { waterProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.White.copy(alpha = 0.5f),
                    strokeCap = StrokeCap.Round
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 3. QUICK ACTION BUTTONS (Natural Tones Grid: rounded-3xl bg-[#F1F1E8]) ---
        Text(
            text = "تسجيل سريع",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            NaturalQuickButton(
                title = "دخول الحمام",
                icon = Icons.Default.Wc,
                modifier = Modifier
                    .weight(1f)
                    .testTag("quick_action_bowel"),
                onClick = { onOpenSheet(SheetType.AddBowel) }
            )
            NaturalQuickButton(
                title = "أدوية وملينات",
                icon = Icons.Default.Medication,
                modifier = Modifier
                    .weight(1f)
                    .testTag("quick_action_medication"),
                onClick = { onOpenSheet(SheetType.AddMedication) }
            )
            NaturalQuickButton(
                title = "وجبات طعام",
                icon = Icons.Default.Restaurant,
                modifier = Modifier
                    .weight(1f)
                    .testTag("quick_action_meal"),
                onClick = { onOpenSheet(SheetType.AddMeal) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Water Bar Button
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onQuickWater(250)
                    Toast.makeText(context, "تمت إضافة كوب ماء (250 مل) 💧", Toast.LENGTH_SHORT).show()
                }
                .testTag("quick_action_water")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalDrink,
                            contentDescription = null,
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "شرب ماء سريع (+250 مل)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "انقر لإضافة كوب ماء فوراً",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "إضافة",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(6.dp).size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // --- 4. RECENT ACTIVITY CARD (Natural Tones: rounded-[2rem] bg-white border) ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header with title & date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "النشاط الأخير",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = dateFormat.format(Date()),
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (todayTimeline.isEmpty()) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(
                                Icons.Default.Spa,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(9.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "بداية يوم نقية",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "لم تسجل أي أحداث اليوم بعد.",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        todayTimeline.forEach { item ->
                            val (dotColor, titleText) = when (item) {
                                is TimelineItem.Bowel -> Pair(
                                    Color(0xFF3A4A2D),
                                    if (item.entry.isSuccess) "إخراج أمعاء ناجح (نوع ${item.entry.bristolType})" else "محاولة إخراج"
                                )
                                is TimelineItem.Medication -> Pair(
                                    Color(0xFFFB923C),
                                    "أخذت ${item.entry.name} (${item.entry.dosage})"
                                )
                                is TimelineItem.Meal -> Pair(
                                    Color(0xFF22C55E),
                                    "${item.entry.mealType}: ${item.entry.foodsDescription.ifEmpty { "وجبة غنية بالألياف" }}"
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(9.dp)
                                        .background(dotColor, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = titleText,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.5.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = timeFormat.format(Date(item.timestamp)),
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    TextButton(
                        onClick = onNavigateToTimeline,
                        modifier = Modifier.align(Alignment.End).testTag("view_all_timeline_btn")
                    ) {
                        Text(
                            text = "عرض السجل كاملاً",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
fun NaturalQuickButton(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(9.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun DailyMetricItem(
    icon: ImageVector,
    label: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = unit,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
