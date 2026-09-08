package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BowelEntry
import com.example.data.model.MealEntry
import com.example.data.model.MedicationEntry
import com.example.data.model.TimelineItem
import com.example.ui.SheetType
import com.example.util.DateRanges
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.delay

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
    onNavigateToPalettePreview: (() -> Unit)? = null,
    isAr: Boolean = true
) {
    val context = LocalContext.current
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            now = System.currentTimeMillis()
            delay(60_000)
        }
    }
    val locale = remember(isAr) { if (isAr) Locale("ar") else Locale.ENGLISH }
    val dateFormat = remember(locale) { SimpleDateFormat("EEEE، d MMMM", locale) }
    val timeFormat = remember(locale) { SimpleDateFormat("hh:mm a", locale) }
    val fullDateFormat = remember(locale) { SimpleDateFormat("d MMMM، hh:mm a", locale) }
    val start = DateRanges.startOfToday()
    val end = DateRanges.startOfNextDay()
    val todayBowels = bowels.filter { it.timestamp in start until end }
    val todayWater = meals.filter { it.timestamp in start until end }.sumOf { it.waterMl }
    val todayTimeline = timeline.filter { it.timestamp in start until end }.take(3)
    val safeGoal = waterGoalMl.coerceAtLeast(250)
    val waterProgress = (todayWater.toFloat() / safeGoal).coerceIn(0f, 1f)
    val hoursSinceLast = lastSuccessfulBowel?.let { ((now - it.timestamp) / 3_600_000L).coerceAtLeast(0) }
    val t: (String, String) -> String = { ar, en -> if (isAr) ar else en }
    val elapsedText = when {
        hoursSinceLast == null -> t("لا يوجد تسجيل بعد", "No entry yet")
        hoursSinceLast < 1 -> t("منذ أقل من ساعة", "Less than an hour ago")
        hoursSinceLast < 24 -> t("منذ $hoursSinceLast ساعة", "$hoursSinceLast hours ago")
        else -> t("منذ ${hoursSinceLast / 24} يوم و${hoursSinceLast % 24} ساعة", "${hoursSinceLast / 24} days, ${hoursSinceLast % 24} hours ago")
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.fillMaxWidth().widthInMax600()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(t("متابعتك اليومية", "Your daily tracker"), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(appName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onNavigateToSettings, modifier = Modifier.testTag("home_settings_button")) {
                        Icon(Icons.Default.Settings, contentDescription = t("الإعدادات", "Settings"), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(dateFormat.format(Date(now)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(Modifier.padding(22.dp)) {
                        Text(t("آخر عملية إخراج", "Last bowel movement"), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Spacer(Modifier.height(8.dp))
                        Text(elapsedText, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            lastSuccessfulBowel?.let { fullDateFormat.format(Date(it.timestamp)) } ?: t("ابدأ بتسجيل أول عملية لتظهر هنا.", "Your first entry will appear here."),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        if (lastSuccessfulBowel != null) {
                            Spacer(Modifier.height(14.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.12f))
                            Spacer(Modifier.height(12.dp))
                            Text(
                                t("بريستول ${lastSuccessfulBowel.bristolType} • الإجهاد: ${lastSuccessfulBowel.strainingLevel}", "Bristol ${lastSuccessfulBowel.bristolType} • Straining: ${lastSuccessfulBowel.strainingLevel}"),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { onOpenSheet(SheetType.AddBowel) },
                    modifier = Modifier.fillMaxWidth().height(56.dp).testTag("quick_action_bowel"),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(10.dp))
                    Text(t("تسجيل عملية إخراج", "Record bowel movement"), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(22.dp))
                Text(t("تسجيل سريع", "Quick tracking"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickAction(t("ماء", "Water"), Icons.Default.LocalDrink, Modifier.weight(1f).testTag("quick_action_water")) {
                        onQuickWater(250)
                        Toast.makeText(context, t("تمت إضافة 250 مل", "Added 250 ml"), Toast.LENGTH_SHORT).show()
                    }
                    QuickAction(t("دواء", "Medicine"), Icons.Default.Medication, Modifier.weight(1f).testTag("quick_action_medication")) { onOpenSheet(SheetType.AddMedication) }
                    QuickAction(t("وجبة", "Meal"), Icons.Default.Restaurant, Modifier.weight(1f).testTag("quick_action_meal")) { onOpenSheet(SheetType.AddMeal) }
                }
                Spacer(Modifier.height(22.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(t("الماء والسوائل اليوم", "Today's fluids"), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            Text("$todayWater / $safeGoal ${t("مل", "ml")}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { waterProgress }, modifier = Modifier.fillMaxWidth().height(7.dp),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(t("الهدف قابل للتعديل من الإعدادات. سجّل ما تشربه فعليًا.", "Set your own goal in Settings. Record what you actually drink."), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.height(22.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(t("النشاط الأخير", "Recent activity"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    TextButton(onClick = onNavigateToTimeline, modifier = Modifier.testTag("view_all_timeline_btn")) {
                        Text(t("عرض الكل", "View all"))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(Modifier.height(4.dp))
                if (todayTimeline.isEmpty()) {
                    Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                        Text(t("لا توجد تسجيلات اليوم. يمكنك البدء في أي وقت.", "No entries today. You can start whenever you're ready."), modifier = Modifier.padding(20.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(horizontal = 18.dp, vertical = 6.dp)) {
                            todayTimeline.forEachIndexed { index, item ->
                                val (icon, title) = when (item) {
                                    is TimelineItem.Bowel -> Icons.Default.Wc to if (item.entry.isSuccess) t("عملية إخراج", "Bowel movement") else t("محاولة دون إخراج", "Attempt without stool")
                                    is TimelineItem.Medication -> Icons.Default.Medication to item.entry.name
                                    is TimelineItem.Meal -> Icons.Default.Restaurant to item.entry.mealType
                                }
                                Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(12.dp))
                                    Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text(timeFormat.format(Date(item.timestamp)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                if (index < todayTimeline.lastIndex) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                TextButton(onClick = onNavigateToReport, modifier = Modifier.align(Alignment.CenterHorizontally).testTag("top_report_btn")) {
                    Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(t("عرض تقرير الطبيب", "View doctor report"))
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

private fun Modifier.widthInMax600(): Modifier = this.then(Modifier.widthIn(max = 600.dp))

@Composable
private fun QuickAction(title: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.height(92.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(9.dp))
            Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
        }
    }
}
