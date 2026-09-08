package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import java.util.Calendar
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
    val now = System.currentTimeMillis()
    val hoursSinceLastBowel = lastSuccessfulBowel?.let {
        ((now - it.timestamp) / (1000 * 60 * 60)).toInt().coerceAtLeast(0)
    }

    // Respect the device's local timezone instead of using a UTC-style modulo calculation.
    val startOfDay = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val todayBowels = bowels.filter { it.timestamp >= startOfDay }
    val todayMeds = medications.filter { it.timestamp >= startOfDay }
    val todayMeals = meals.filter { it.timestamp >= startOfDay }
    val todayWater = todayMeals.sumOf { it.waterMl }
    val waterProgress = if (waterGoalMl > 0) {
        (todayWater.toFloat() / waterGoalMl.toFloat()).coerceIn(0f, 1f)
    } else 0f
    val todayTimeline = timeline.filter { it.timestamp >= startOfDay }.take(3)
    val timeFormat = SimpleDateFormat("hh:mm a", Locale("ar"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header(
            appName = appName,
            onNavigateToReport = onNavigateToReport,
            onNavigateToSettings = onNavigateToSettings
        )

        StatusCard(
            lastSuccessfulBowel = lastSuccessfulBowel,
            hoursSinceLastBowel = hoursSinceLastBowel,
            todayBowels = todayBowels.size,
            todayMeds = todayMeds.size,
            todayWater = todayWater,
            waterGoalMl = waterGoalMl,
            waterProgress = waterProgress,
            onAddBowel = { onOpenSheet(SheetType.AddBowel) }
        )

        Text(
            text = "تسجيل سريع",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SecondaryAction(
                title = "دواء",
                icon = Icons.Default.Medication,
                modifier = Modifier.weight(1f).testTag("quick_action_medication"),
                onClick = { onOpenSheet(SheetType.AddMedication) }
            )
            SecondaryAction(
                title = "وجبة",
                icon = Icons.Default.Restaurant,
                modifier = Modifier.weight(1f).testTag("quick_action_meal"),
                onClick = { onOpenSheet(SheetType.AddMeal) }
            )
            SecondaryAction(
                title = "+250 مل ماء",
                icon = Icons.Default.LocalDrink,
                modifier = Modifier.weight(1f).testTag("quick_action_water"),
                onClick = {
                    onQuickWater(250)
                    Toast.makeText(context, "تمت إضافة 250 مل ماء", Toast.LENGTH_SHORT).show()
                }
            )
        }

        RecentActivityCard(
            items = todayTimeline,
            timeFormat = timeFormat,
            onNavigateToTimeline = onNavigateToTimeline
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun Header(
    appName: String,
    onNavigateToReport: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "اليوم",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = appName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton(
                onClick = onNavigateToReport,
                modifier = Modifier.testTag("top_report_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "تقرير الطبيب",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(
                onClick = onNavigateToSettings,
                modifier = Modifier.testTag("home_settings_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "الإعدادات",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StatusCard(
    lastSuccessfulBowel: BowelEntry?,
    hoursSinceLastBowel: Int?,
    todayBowels: Int,
    todayMeds: Int,
    todayWater: Int,
    waterGoalMl: Int,
    waterProgress: Float,
    onAddBowel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "آخر عملية إخراج",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = when {
                    hoursSinceLastBowel == null -> "لا يوجد تسجيل بعد"
                    hoursSinceLastBowel < 1 -> "منذ أقل من ساعة"
                    hoursSinceLastBowel < 24 -> "منذ $hoursSinceLastBowel ساعة"
                    else -> "منذ ${hoursSinceLastBowel / 24} يوم"
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (lastSuccessfulBowel != null) {
                Text(
                    text = "بريستول ${lastSuccessfulBowel.bristolType} • الإجهاد: ${lastSuccessfulBowel.strainingLevel}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = onAddBowel,
                modifier = Modifier.fillMaxWidth().testTag("quick_action_bowel"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(Icons.Default.Wc, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("تسجيل عملية إخراج", fontWeight = FontWeight.Bold)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Metric("الإخراج", todayBowels.toString())
                Metric("الأدوية", todayMeds.toString())
                Metric("الماء", "$todayWater مل")
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "السوائل اليوم",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$todayWater / $waterGoalMl مل",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                LinearProgressIndicator(
                    progress = { waterProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(50)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

@Composable
private fun Metric(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SecondaryAction(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp, vertical = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun RecentActivityCard(
    items: List<TimelineItem>,
    timeFormat: SimpleDateFormat,
    onNavigateToTimeline: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "النشاط اليوم",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                TextButton(
                    onClick = onNavigateToTimeline,
                    modifier = Modifier.testTag("view_all_timeline_btn")
                ) {
                    Text("عرض الكل", color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (items.isEmpty()) {
                Text(
                    text = "لم تسجل أحداثًا اليوم بعد.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 14.dp)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items.forEach { item ->
                        val title = when (item) {
                            is TimelineItem.Bowel -> if (item.entry.isSuccess) {
                                "عملية إخراج • بريستول ${item.entry.bristolType}"
                            } else "محاولة إخراج"
                            is TimelineItem.Medication -> "${item.entry.name} • ${item.entry.dosage}"
                            is TimelineItem.Meal -> item.entry.mealType
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(8.dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary
                            ) {}
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = timeFormat.format(Date(item.timestamp)),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
