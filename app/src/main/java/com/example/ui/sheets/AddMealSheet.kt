package com.example.ui.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MealEntry

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddMealSheet(
    onDismiss: () -> Unit,
    onSave: (MealEntry) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var mealType by remember { mutableStateOf("غداء") }
    var foodsDescription by remember { mutableStateOf("") }
    var fiberLevel by remember { mutableStateOf("متوسط الألياف") }
    var waterMl by remember { mutableIntStateOf(250) }
    var triggersSuspected by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val mealTypes = listOf("إفطار", "غداء", "عشاء", "وجبة خفيفة", "سوائل فقط")
    val fiberOptions = listOf("غني بالألياف", "متوسط الألياف", "قليل الألياف")
    val waterOptions = listOf(0, 250, 500, 750, 1000)

    val quickFoodTags = listOf(
        "سلطة خضراء",
        "شوربة خضار",
        "شوفان وبذور",
        "فواكه مجففة (قراصيا/تين)",
        "بقوليات وعدس",
        "خبز أبيض",
        "خبز نخالة كامل",
        "أرز أبيض",
        "وجبة سريعة ومقليات",
        "لحوم ودواجن",
        "ألبان وأجبان"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "تسجيل وجبة طعام وسوائل",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "إغلاق")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Meal Type
            Text(
                text = "نوع الوجبة وموعدها:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                mealTypes.forEach { type ->
                    FilterChip(
                        selected = mealType == type,
                        onClick = { mealType = type },
                        label = { Text(type, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Food Suggestions
            Text(
                text = "اقتراحات سريعة للأطعمة:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quickFoodTags.forEach { tag ->
                    FilterChip(
                        selected = foodsDescription.contains(tag),
                        onClick = {
                            foodsDescription = if (foodsDescription.isBlank()) {
                                tag
                            } else if (foodsDescription.contains(tag)) {
                                foodsDescription.replace(tag, "").replace("، ،", "،").trim(' ', '،')
                            } else {
                                "$foodsDescription، $tag"
                            }
                        },
                        label = { Text(tag, fontSize = 11.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Foods Description TextField
            OutlinedTextField(
                value = foodsDescription,
                onValueChange = { foodsDescription = it },
                label = { Text("الأطعمة التي تم تناولها بدقة") },
                placeholder = { Text("مثال: صحن عدس مع سلطة ورقية، شريحة توست أسمر...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fiber Level
            Text(
                text = "مستوى الألياف في الوجبة:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                fiberOptions.forEach { opt ->
                    FilterChip(
                        selected = fiberLevel == opt,
                        onClick = { fiberLevel = opt },
                        label = { Text(opt, fontSize = 12.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Water intake
            Text(
                text = "كمية شرب الماء والسوائل المرافقة (مل):",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                waterOptions.forEach { ml ->
                    FilterChip(
                        selected = waterMl == ml,
                        onClick = { waterMl = ml },
                        label = { Text(if (ml == 0) "بدون" else "$ml مل", fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Triggers suspected
            Text(
                text = "مسببات إمساك محتملة بالوجبة (اختياري):",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = triggersSuspected,
                onValueChange = { triggersSuspected = it },
                placeholder = { Text("مثلاً: خبز أبيض مفرط، شاي داكن، جبن شيدر، قلة ماء...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Notes
            Text(
                text = "ملاحظات للطبيب:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = { Text("أي أعراض بعد الأكل (مثل انتفاخ، ثقل، غازات)...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onSave(
                        MealEntry(
                            timestamp = System.currentTimeMillis(),
                            mealType = mealType,
                            foodsDescription = foodsDescription.trim(),
                            fiberLevel = fiberLevel,
                            waterMl = waterMl,
                            triggersSuspected = triggersSuspected.trim(),
                            notes = notes.trim()
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_meal_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "حفظ الوجبة في السجل",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
