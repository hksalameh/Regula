package com.example.ui.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.data.model.BowelEntry
import com.example.ui.components.BristolTypeSelector

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddBowelSheet(
    onDismiss: () -> Unit,
    onSave: (BowelEntry) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isSuccess by remember { mutableStateOf(true) }
    var bristolType by remember { mutableIntStateOf(2) }
    var strainingLevel by remember { mutableStateOf("متوسط") }
    var durationMinutes by remember { mutableIntStateOf(15) }
    var incompleteEvacuation by remember { mutableStateOf(true) }
    var hasBlood by remember { mutableStateOf(false) }
    var bloodNotes by remember { mutableStateOf("") }
    var painLevel by remember { mutableFloatStateOf(3f) }
    var selectedSymptoms by remember { mutableStateOf(setOf<String>()) }
    var notes by remember { mutableStateOf("") }

    val commonSymptoms = listOf(
        "انتفاخ وغازات",
        "مغص معوي",
        "ألم أو شق شرجي",
        "ثقل وامتلاء بالبطن",
        "شعور بانسداد",
        "غثيان"
    )

    val strainingOptions = listOf("بدون إجهاد", "خفيف", "متوسط", "شديد")
    val durations = listOf(5, 10, 15, 20, 30, 45)

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
                    text = "تسجيل دخول الحمام والتبرز",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "إغلاق")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 1: Was it successful bowel movement or failed attempt?
            Text(
                text = "نتيجة دخول الحمام:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { isSuccess = true },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSuccess) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "✅ تم التبرز بنجاح",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "خرج براز (كامل أو جزئي)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { isSuccess = false },
                    colors = CardDefaults.cardColors(
                        containerColor = if (!isSuccess) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "❌ محاولة دون خروج",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (!isSuccess) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "إلحاح كاذب أو تعذر الإخراج",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bristol Scale if successful
            if (isSuccess) {
                BristolTypeSelector(
                    selectedType = bristolType,
                    onTypeSelected = { bristolType = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "توثيق محاولات التبرز الفاشلة مهم جداً للطبيب لمعرفة وتيرة الإلحاح الكاذب وعسر الإخراج.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Straining Level
            Text(
                text = "درجة الحزق والإجهاد أثناء المحاولة:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                strainingOptions.forEach { opt ->
                    FilterChip(
                        selected = strainingLevel == opt,
                        onClick = { strainingLevel = opt },
                        label = { Text(opt, fontSize = 12.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Duration in bathroom
            Text(
                text = "المدة المستغرقة في الحمام (بالدقائق):",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                durations.forEach { d ->
                    FilterChip(
                        selected = durationMinutes == d,
                        onClick = { durationMinutes = d },
                        label = { Text("$d د", fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Incomplete evacuation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "الشعور بعدم الإفراغ الكامل:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "إحساس مستمر بوجود بقايا داخل المستقيم بعد الانتهاء",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Switch(
                    checked = incompleteEvacuation,
                    onCheckedChange = { incompleteEvacuation = it }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Blood in stool
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "هل لاحظت أي دم؟",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (hasBlood) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "قطرات دم فاتح على ورق الحمام أو مع البراز",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Switch(
                    checked = hasBlood,
                    onCheckedChange = { hasBlood = it }
                )
            }

            if (hasBlood) {
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = bloodNotes,
                    onValueChange = { bloodNotes = it },
                    placeholder = { Text("صف كمية ولون الدم (مثلاً: قطرات حمراء فاتحة عند المسح)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pain Level Slider
            Text(
                text = "مستوى الألم أو الانزعاج (0 إلى 10): ${painLevel.toInt()}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Slider(
                value = painLevel,
                onValueChange = { painLevel = it },
                valueRange = 0f..10f,
                steps = 9
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Common symptoms
            Text(
                text = "أعراض مصاحبة أخرى:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                commonSymptoms.forEach { symptom ->
                    val isChecked = selectedSymptoms.contains(symptom)
                    FilterChip(
                        selected = isChecked,
                        onClick = {
                            selectedSymptoms = if (isChecked) {
                                selectedSymptoms - symptom
                            } else {
                                selectedSymptoms + symptom
                            }
                        },
                        label = { Text(symptom, fontSize = 12.sp) },
                        leadingIcon = if (isChecked) {
                            { Icon(Icons.Default.Check, contentDescription = null) }
                        } else null
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Doctor Notes
            Text(
                text = "ملاحظات إضافية للدكتور:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = { Text("أي تفاصيل إضافية تريد أن ينتبه لها الطبيب...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    onSave(
                        BowelEntry(
                            timestamp = System.currentTimeMillis(),
                            isSuccess = isSuccess,
                            bristolType = if (isSuccess) bristolType else 1,
                            strainingLevel = strainingLevel,
                            durationMinutes = durationMinutes,
                            incompleteEvacuation = incompleteEvacuation,
                            stoolColor = if (isSuccess) "بني طبيعي" else "بدون خروج",
                            hasBlood = hasBlood,
                            bloodNotes = bloodNotes,
                            painLevel = painLevel.toInt(),
                            symptoms = selectedSymptoms.joinToString("، "),
                            notes = notes
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_bowel_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "حفظ وإضافة للسجل الطبي",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
