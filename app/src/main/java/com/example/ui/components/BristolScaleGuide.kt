package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.ui.theme.BristolConstipationMild
import com.example.ui.theme.BristolConstipationSevere
import com.example.ui.theme.BristolDiarrhea
import com.example.ui.theme.BristolIdeal
import com.example.ui.theme.BristolLoose

data class BristolTypeInfo(
    val type: Int,
    val title: String,
    val description: String,
    val status: String,
    val color: Color,
    val isConstipation: Boolean
)

val BristolTypes = listOf(
    BristolTypeInfo(
        type = 1,
        title = "النوع 1: كتل صلبة منفصلة",
        description = "كتل صغيرة متفرقة وشديدة الصلابة تشبه البندق أو حبات الجوز، خروجها مجهد ومؤلم جداً.",
        status = "إمساك شديد وحاد",
        color = BristolConstipationSevere,
        isConstipation = true
    ),
    BristolTypeInfo(
        type = 2,
        title = "النوع 2: سجقي متكتل ومتصلب",
        description = "كتلة واحدة تشبه السجق ولكنها متكتلة ومتصلبة ومضغوطة بصعوبة.",
        status = "إمساك متوسط",
        color = BristolConstipationMild,
        isConstipation = true
    ),
    BristolTypeInfo(
        type = 3,
        title = "النوع 3: سجقي مع تشققات بالسطح",
        description = "قوام يشبه السجق مع وجود تشققات سطحية واضحة، متماسك ويميل للجفاف قليلاً.",
        status = "طبيعي (يميل للإمساك)",
        color = Color(0xFF6B7280),
        isConstipation = false
    ),
    BristolTypeInfo(
        type = 4,
        title = "النوع 4: سجقي ناعم وأملس",
        description = "يشبه السجق أو الثعبان، ناعم وسلس وطري ويسهل خروجه بدون أي إجهاد.",
        status = "المثالي والصحي تماماً",
        color = BristolIdeal,
        isConstipation = false
    ),
    BristolTypeInfo(
        type = 5,
        title = "النوع 5: قطع رخوة بحواف محددة",
        description = "قطع طرية ورخوة ذات حواف واضحة تمر بسهولة (قد يشير لنقص ألياف).",
        status = "يميل للرخاوة",
        color = BristolLoose,
        isConstipation = false
    ),
    BristolTypeInfo(
        type = 6,
        title = "النوع 6: قطع رقيقة ومفككة",
        description = "قطع رخوة ومفككة بحواف خشنة وغير منتظمة، شبه سائل.",
        status = "إسهال خفيف",
        color = BristolLoose,
        isConstipation = false
    ),
    BristolTypeInfo(
        type = 7,
        title = "النوع 7: سائل مائي بالكامل",
        description = "سائل بالكامل دون أي قطع صلبة، خروج مائي مستمر.",
        status = "إسهال مائي",
        color = BristolDiarrhea,
        isConstipation = false
    )
)

/**
 * Compact Dropdown selector for Bristol Stool Scale.
 * Clicking opens the list of 7 types, and selecting any immediately closes the list.
 */
@Composable
fun BristolTypeSelector(
    selectedType: Int,
    onTypeSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    val currentTypeInfo = BristolTypes.firstOrNull { it.type == selectedType } ?: BristolTypes[1]

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "قوام البراز (مقياس بريستول الطبي):",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "انقر لتغيير النوع",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Selected item dropdown trigger card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("bristol_dropdown_trigger")
                .clickable { showDialog = true },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            border = BorderStroke(1.5.dp, currentTypeInfo.color.copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(currentTypeInfo.color.copy(alpha = 0.2f))
                        .border(1.dp, currentTypeInfo.color, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${currentTypeInfo.type}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = currentTypeInfo.color
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentTypeInfo.title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${currentTypeInfo.status} — ${currentTypeInfo.description.take(45)}...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "قائمة منسدلة",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }

    // Modal Dropdown Dialog showing all 7 types; tapping one selects and instantly closes
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "اختر قوام البراز (1 إلى 7)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = { showDialog = false }) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق")
                    }
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(BristolTypes, key = { it.type }) { item ->
                        val isSelected = item.type == selectedType
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onTypeSelected(item.type)
                                    showDialog = false // Closes immediately after selection!
                                }
                                .border(
                                    width = if (isSelected) 2.dp else 0.5.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                                else MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(item.color.copy(alpha = 0.2f))
                                        .border(1.dp, item.color, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${item.type}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = item.color
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = item.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = item.color.copy(alpha = 0.15f)
                                        ) {
                                            Text(
                                                text = item.status,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = item.color,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = item.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.5.sp
                                    )
                                }
                                if (isSelected) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "تم الاختيار",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("إلغاء")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}
