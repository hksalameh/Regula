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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MedicationEntry

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddMedicationSheet(
    onDismiss: () -> Unit,
    onSave: (MedicationEntry) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("ملين أسموزي") }
    var dosage by remember { mutableStateOf("كيس واحد") }
    var effectObserved by remember { mutableStateOf("قيد الانتظار") }
    var notes by remember { mutableStateOf("") }

    val commonMeds = listOf(
        "موفيكول (PEG 3350)" to "ملين أسموزي",
        "دوفالاك (لاكتولوز)" to "ملين أسموزي",
        "بيساكوديل (دلكولاكس)" to "ملين منبه",
        "سنامكي (عشبة السنا)" to "ملين منبه",
        "ألياف القاطونة (Psyllium)" to "مكمل ألياف حجمي",
        "تحاميل جليسرين" to "تحميلة موضعية",
        "حقنة شرجية (Enema)" to "حقنة شرجية",
        "سترات الماغنيسيوم" to "ملين ملحي أسموزي"
    )

    val commonDosages = listOf("كيس واحد", "كيسان", "15 مل", "30 مل", "قرص واحد", "قرصان", "ملعقة كبيرة", "تحميلة واحدة")
    val effectOptions = listOf("أحدث إخراج", "إخراج جزئي", "لم يحدث تأثير", "سبب مغص وغازات", "قيد الانتظار")

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
                    text = "تسجيل دواء أو ملين",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "إغلاق")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Selection Chips
            Text(
                text = "اختر من الملينات والأدوية الشائعة:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                commonMeds.forEach { (medName, medCat) ->
                    FilterChip(
                        selected = name == medName,
                        onClick = {
                            name = medName
                            category = medCat
                        },
                        label = { Text(medName, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Medication Name Custom
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("اسم الدواء أو الملين") },
                placeholder = { Text("مثال: موفيكول، لاكتولوز، مكمل ألياف...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Dosage Chips & Field
            Text(
                text = "الجرعة المتناولة:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                commonDosages.forEach { d ->
                    FilterChip(
                        selected = dosage == d,
                        onClick = { dosage = d },
                        label = { Text(d, fontSize = 12.sp) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = dosage,
                onValueChange = { dosage = it },
                label = { Text("تفاصيل الجرعة (اختياري)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Effect observed
            Text(
                text = "الأثر أو الاستجابة بعد أخذ الملين:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                effectOptions.forEach { eff ->
                    FilterChip(
                        selected = effectObserved == eff,
                        onClick = { effectObserved = eff },
                        label = { Text(eff, fontSize = 12.sp) }
                    )
                }
            }

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
                placeholder = { Text("مثلاً: أخذته مع كوبين ماء قبل النوم، سبب لي نفخة خفيفة...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(
                            MedicationEntry(
                                timestamp = System.currentTimeMillis(),
                                name = name.trim(),
                                category = category,
                                dosage = dosage.trim(),
                                effectObserved = effectObserved,
                                notes = notes.trim()
                            )
                        )
                    }
                },
                enabled = name.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_medication_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "حفظ الدواء في السجل",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
