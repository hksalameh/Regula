package com.example.ui.screens

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.ui.ThemeMode
import com.example.ui.theme.AppColorPalette

@Composable
fun SettingsScreen(
    currentThemeMode: ThemeMode,
    currentColorPalette: AppColorPalette,
    appNameAlias: String,
    appLanguage: String,
    waterGoalMl: Int,
    onThemeModeChange: (ThemeMode) -> Unit,
    onColorPaletteChange: (AppColorPalette) -> Unit,
    onAppNameAliasChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit,
    onWaterGoalChange: (Int) -> Unit,
    onReseedDemoData: () -> Unit,
    onClearAllData: () -> Unit,
    hasRecords: Boolean = false,
    onNavigateToBristol: () -> Unit = {},
    onExportData: (() -> Unit)? = null,
    onImportData: (() -> Unit)? = null
) {
    val isAr = appLanguage == "AR"
    val t: (String, String) -> String = { ar, en -> if (isAr) ar else en }
    var showClearDialog by remember { mutableStateOf(false) }
    var showDemoDialog by remember { mutableStateOf(false) }
    var showPaletteMenu by remember { mutableStateOf(false) }
    var showPalettePreview by remember { mutableStateOf(false) }
    var alias by remember(appNameAlias) { mutableStateOf(appNameAlias) }

    if (showPalettePreview) {
        ColorPalettePreviewScreen(
            currentPalette = currentColorPalette,
            appLanguage = appLanguage,
            onSelectAndApply = { onColorPaletteChange(it); showPalettePreview = false },
            onBack = { showPalettePreview = false }
        )
        return
    }

    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.TopCenter) {
        Column(Modifier.fillMaxWidth().widthIn(max = 600.dp).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 20.dp)) {
            Text(t("الإعدادات", "Settings"), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(t("اضبط التطبيق بالطريقة التي تناسبك.", "Make the tracker work for you."), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(22.dp))

            SettingsCard(t("المظهر واللغة", "Appearance & language")) {
                Text(t("لغة التطبيق", "App language"), style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = isAr, onClick = { onLanguageChange("AR") }, label = { Text("العربية") })
                    FilterChip(selected = !isAr, onClick = { onLanguageChange("EN") }, label = { Text("English") })
                }
                Spacer(Modifier.height(12.dp))
                Text(t("وضع العرض", "Display mode"), style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(ThemeMode.LIGHT, ThemeMode.DARK, ThemeMode.SYSTEM).forEach { mode ->
                        FilterChip(
                            selected = currentThemeMode == mode,
                            onClick = { onThemeModeChange(mode) },
                            label = { Text(when (mode) {
                                ThemeMode.LIGHT -> t("فاتح", "Light")
                                ThemeMode.DARK -> t("داكن", "Dark")
                                ThemeMode.SYSTEM -> t("تلقائي", "System")
                            }) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(t("لوحة الألوان", "Color palette"), style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Box {
                    OutlinedButton(onClick = { showPaletteMenu = true }, modifier = Modifier.fillMaxWidth()) {
                        Box(Modifier.size(18.dp).background(currentColor(currentColorPalette), CircleShape))
                        Spacer(Modifier.width(10.dp))
                        Text(if (isAr) currentColorPalette.titleAr else currentColorPalette.titleEn)
                    }
                    DropdownMenu(expanded = showPaletteMenu, onDismissRequest = { showPaletteMenu = false }) {
                        AppColorPalette.entries.forEach { palette ->
                            DropdownMenuItem(
                                text = { Text(if (isAr) palette.titleAr else palette.titleEn) },
                                leadingIcon = { Box(Modifier.size(18.dp).background(currentColor(palette), CircleShape)) },
                                trailingIcon = { if (currentColorPalette == palette) Icon(Icons.Default.Check, contentDescription = null) },
                                onClick = { onColorPaletteChange(palette); showPaletteMenu = false }
                            )
                        }
                    }
                }
                TextButton(onClick = { showPalettePreview = true }, modifier = Modifier.align(Alignment.End)) {
                    Text(t("معاينة الألوان", "Preview palettes"))
                }
            }
            Spacer(Modifier.height(14.dp))

            SettingsCard(t("الاسم والهدف اليومي", "Name & daily goal")) {
                OutlinedTextField(
                    value = alias,
                    onValueChange = { alias = it.take(40) },
                    label = { Text(t("اسم التطبيق", "App name")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (alias != appNameAlias && alias.isNotBlank()) {
                    TextButton(onClick = { onAppNameAliasChange(alias.trim()) }, modifier = Modifier.align(Alignment.End)) {
                        Text(t("حفظ الاسم", "Save name"))
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(t("هدف الماء والسوائل", "Fluid tracking goal"), style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(4.dp))
                Text(t("اختر هدفًا شخصيًا يناسب إرشادات طبيبك. ليس كل شخص بحاجة إلى الكمية نفسها.", "Choose a personal goal appropriate for your clinician's advice. Fluid needs vary."), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onWaterGoalChange(waterGoalMl - 250) }, enabled = waterGoalMl > 250) {
                        Icon(Icons.Default.Remove, contentDescription = t("تقليل الهدف", "Decrease goal"))
                    }
                    Text("$waterGoalMl ${t("مل", "ml")}", modifier = Modifier.padding(horizontal = 12.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { onWaterGoalChange(waterGoalMl + 250) }, enabled = waterGoalMl < 10000) {
                        Icon(Icons.Default.Add, contentDescription = t("زيادة الهدف", "Increase goal"))
                    }
                }
            }
            Spacer(Modifier.height(14.dp))

            SettingsCard(t("الدليل", "Reference")) {
                OutlinedButton(onClick = onNavigateToBristol, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(t("دليل مقياس بريستول", "Bristol stool chart"))
                }
            }
            Spacer(Modifier.height(14.dp))

            SettingsCard(t("البيانات والخصوصية", "Data & privacy")) {
                Text(t("السجلات محفوظة على هذا الجهاز. قد يؤدي حذف التطبيق أو فقدان الهاتف إلى فقدانها. احفظ نسخة في مكان آمن إذا احتجت إليها.", "Records are stored on this device. Uninstalling the app or losing the phone may erase them. Keep a secure copy if needed."), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (onExportData != null) {
                    Spacer(Modifier.height(12.dp))
                    OutlinedButton(onClick = onExportData, modifier = Modifier.fillMaxWidth().testTag("export_data_button")) {
                        Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(t("تصدير نسخة من السجلات", "Export records"))
                    }
                }
                if (onImportData != null) {
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = onImportData, modifier = Modifier.fillMaxWidth().testTag("import_data_button")) {
                        Icon(Icons.Default.FileUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(t("استيراد نسخة احتياطية", "Import backup"))
                    }
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(onClick = { showDemoDialog = true }, enabled = !hasRecords, modifier = Modifier.fillMaxWidth()) {
                    Text(t("إضافة بيانات تجريبية", "Add sample data"))
                }
                if (hasRecords) {
                    Text(t("البيانات التجريبية متاحة فقط عندما تكون جميع السجلات فارغة. لن تُستبدل بياناتك الحالية.", "Sample data is available only when all records are empty. Existing records are never replaced."), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { showClearDialog = true }, modifier = Modifier.fillMaxWidth().testTag("clear_data_button")) {
                    Icon(Icons.Default.DeleteForever, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(t("مسح جميع السجلات", "Clear all records"), color = MaterialTheme.colorScheme.error)
                }
            }
            Spacer(Modifier.height(20.dp))
            Text("Regula • 1.0.1", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(20.dp))
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(t("مسح جميع السجلات؟", "Clear all records?")) },
            text = { Text(t("سيُحذف سجل الإخراج والأدوية والوجبات نهائيًا من هذا الجهاز. لا يمكن التراجع عن ذلك. صدّر نسخة أولًا إذا كنت تريد الاحتفاظ بها.", "All bowel, medication and meal records will be permanently deleted from this device. This cannot be undone. Export a backup first if you want to keep them.")) },
            confirmButton = { TextButton(onClick = { onClearAllData(); showClearDialog = false }) { Text(t("مسح الكل", "Clear all"), color = MaterialTheme.colorScheme.error) } },
            dismissButton = { TextButton(onClick = { showClearDialog = false }) { Text(t("إلغاء", "Cancel")) } }
        )
    }
    if (showDemoDialog) {
        AlertDialog(
            onDismissRequest = { showDemoDialog = false },
            title = { Text(t("إضافة بيانات تجريبية؟", "Add sample data?")) },
            text = { Text(t("ستُضاف سجلات افتراضية لتجربة التطبيق فقط. ليست بياناتك الطبية، ولن تُضاف إذا كانت لديك سجلات حالية.", "Fictitious records will be added for demonstration only. They are not your medical history and will not be added if any records already exist.")) },
            confirmButton = { TextButton(onClick = { onReseedDemoData(); showDemoDialog = false }) { Text(t("إضافة", "Add")) } },
            dismissButton = { TextButton(onClick = { showDemoDialog = false }) { Text(t("إلغاء", "Cancel")) } }
        )
    }
}

@Composable
private fun SettingsCard(title: String, content: @Composable Column.() -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
        Column(Modifier.padding(18.dp), content = {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(14.dp))
            content()
        })
    }
}

private fun currentColor(palette: AppColorPalette) = palette.previewColors.first()
