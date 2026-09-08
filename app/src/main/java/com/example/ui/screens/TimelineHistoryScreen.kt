package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TimelineItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TimelineHistoryScreen(
    timelineItems: List<TimelineItem>,
    onDeleteBowel: (Long) -> Unit,
    onDeleteMedication: (Long) -> Unit,
    onDeleteMeal: (Long) -> Unit
) {
    var activeFilter by remember { mutableStateOf("الكل") }
    var itemToDelete by remember { mutableStateOf<TimelineItem?>(null) }

    val filteredItems = when (activeFilter) {
        "حمام فقط" -> timelineItems.filterIsInstance<TimelineItem.Bowel>()
        "أدوية وملينات" -> timelineItems.filterIsInstance<TimelineItem.Medication>()
        "وجبات وسوائل" -> timelineItems.filterIsInstance<TimelineItem.Meal>()
        else -> timelineItems
    }

    val timeFormat = SimpleDateFormat("hh:mm a", Locale("ar"))
    val dateFormat = SimpleDateFormat("EEEE d MMMM yyyy", Locale("ar"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "السجل الزمني للأحداث",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "ربط زمني دقيق بين الوجبات والأدوية وحركات الأمعاء",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Filter chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("الكل", "حمام فقط", "أدوية وملينات", "وجبات وسوائل").forEach { filter ->
                FilterChip(
                    selected = activeFilter == filter,
                    onClick = { activeFilter = filter },
                    label = { Text(filter, fontSize = 12.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "لا توجد عناصر مسجلة لهذا التصنيف",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredItems, key = { "${it.javaClass.simpleName}_${it.id}" }) { item ->
                    TimelineItemCard(
                        item = item,
                        timeFormat = timeFormat,
                        dateFormat = dateFormat,
                        onDeleteClick = { itemToDelete = item }
                    )
                }
            }
        }
    }

    // Delete confirmation dialog
    if (itemToDelete != null) {
        val item = itemToDelete!!
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text("حذف هذا السجل؟", fontWeight = FontWeight.Bold) },
            text = { Text("هل تريد بالتأكيد حذف هذا السجل من قاعدة البيانات؟ لا يمكن التراجع عن هذا الإجراء.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        when (item) {
                            is TimelineItem.Bowel -> onDeleteBowel(item.id)
                            is TimelineItem.Medication -> onDeleteMedication(item.id)
                            is TimelineItem.Meal -> onDeleteMeal(item.id)
                        }
                        itemToDelete = null
                    }
                ) {
                    Text("حذف", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@Composable
fun TimelineItemCard(
    item: TimelineItem,
    timeFormat: SimpleDateFormat,
    dateFormat: SimpleDateFormat,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Date & Time + Delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val (icon, bg, tint) = when (item) {
                        is TimelineItem.Bowel -> Triple(
                            Icons.Default.Wc,
                            if (item.entry.isSuccess) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                            if (item.entry.isSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                        is TimelineItem.Medication -> Triple(
                            Icons.Default.Medication,
                            Color(0xFFFFE0B2),
                            Color(0xFFE65100)
                        )
                        is TimelineItem.Meal -> Triple(
                            Icons.Default.Restaurant,
                            Color(0xFFE8F5E9),
                            Color(0xFF2E7D32)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(bg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = when (item) {
                                is TimelineItem.Bowel -> if (item.entry.isSuccess) "دخول الحمام: تبرز ناجح" else "دخول الحمام: محاولة دون تبرز"
                                is TimelineItem.Medication -> "دواء / ملين: ${item.entry.name}"
                                is TimelineItem.Meal -> "وجبة: ${item.entry.mealType}"
                            },
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${dateFormat.format(Date(item.timestamp))} • ${timeFormat.format(Date(item.timestamp))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.DeleteOutline,
                        contentDescription = "حذف",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Body based on event type
            when (item) {
                is TimelineItem.Bowel -> {
                    val b = item.entry
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (b.isSuccess) {
                            BadgeChip(
                                text = "بريستول ${b.bristolType}",
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            BadgeChip(
                                text = "إلحاح كاذب",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        BadgeChip(
                            text = "إجهاد: ${b.strainingLevel}",
                            color = if (b.strainingLevel == "شديد") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                        )
                        BadgeChip(
                            text = "${b.durationMinutes} دقيقة",
                            color = MaterialTheme.colorScheme.outline
                        )
                        if (b.incompleteEvacuation) {
                            BadgeChip(
                                text = "عدم إفراغ كامل",
                                color = Color(0xFFD97706)
                            )
                        }
                    }

                    if (b.hasBlood) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "دم: ${if (b.bloodNotes.isNotBlank()) b.bloodNotes else "نعم، لوحظ دم"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    if (b.painLevel > 0 || b.symptoms.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ألم: ${b.painLevel}/10 ${if (b.symptoms.isNotBlank()) "• أعراض: ${b.symptoms}" else ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (b.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ملاحظة: ${b.notes}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                is TimelineItem.Medication -> {
                    val m = item.entry
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        BadgeChip(text = m.category, color = Color(0xFFE65100))
                        if (m.dosage.isNotBlank()) {
                            BadgeChip(text = "جرعة: ${m.dosage}", color = MaterialTheme.colorScheme.outline)
                        }
                        BadgeChip(text = "الأثر: ${m.effectObserved}", color = MaterialTheme.colorScheme.primary)
                    }
                    if (m.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "ملاحظة: ${m.notes}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                is TimelineItem.Meal -> {
                    val meal = item.entry
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        BadgeChip(text = meal.fiberLevel, color = Color(0xFF2E7D32))
                        if (meal.waterMl > 0) {
                            BadgeChip(text = "${meal.waterMl} مل ماء", color = Color(0xFF0288D1))
                        }
                    }
                    if (meal.foodsDescription.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "الأطعمة: ${meal.foodsDescription}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (meal.triggersSuspected.isNotBlank()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "مسببات محتملة: ${meal.triggersSuspected}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFB42824)
                        )
                    }
                    if (meal.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "ملاحظة: ${meal.notes}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeChip(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, color.copy(alpha = 0.3f))
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = color,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
        )
    }
}
