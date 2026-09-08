package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.SheetType
import com.example.ui.ThemeMode
import com.example.ui.screens.BristolReferenceScreen
import com.example.ui.screens.ColorPalettePreviewScreen
import com.example.ui.screens.DoctorReportScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TimelineHistoryScreen
import com.example.ui.screens.TodayHomeScreen
import com.example.ui.sheets.AddBowelSheet
import com.example.ui.sheets.AddMealSheet
import com.example.ui.sheets.AddMedicationSheet
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            val colorPalette by viewModel.colorPalette.collectAsStateWithLifecycle()
            val systemDark = isSystemInDarkTheme()
            val isDark = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> systemDark
            }

            MyApplicationTheme(darkTheme = isDark, palette = colorPalette) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    MainAppContent(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val activeSheet by viewModel.activeSheet.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val colorPalette by viewModel.colorPalette.collectAsStateWithLifecycle()
    val appNameAlias by viewModel.appNameAlias.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val waterGoalMl by viewModel.waterGoalMl.collectAsStateWithLifecycle()
    val isAr = appLanguage == "AR"

    val bowels by viewModel.bowelEntries.collectAsStateWithLifecycle()
    val medications by viewModel.medicationEntries.collectAsStateWithLifecycle()
    val meals by viewModel.mealEntries.collectAsStateWithLifecycle()
    val timeline by viewModel.unifiedTimeline.collectAsStateWithLifecycle()
    val lastSuccessfulBowel by viewModel.lastSuccessfulBowel.collectAsStateWithLifecycle()

    var showPalettePreviewScreen by remember { mutableStateOf(false) }

    if (showPalettePreviewScreen) {
        ColorPalettePreviewScreen(
            currentPalette = colorPalette,
            appLanguage = appLanguage,
            onSelectAndApply = { newPalette ->
                viewModel.setColorPalette(newPalette)
                showPalettePreviewScreen = false
            },
            onBack = { showPalettePreviewScreen = false }
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { viewModel.selectTab(0) },
                        icon = { Icon(Icons.Default.Today, contentDescription = if (isAr) "اليوم" else "Today") },
                        label = { Text(if (isAr) "اليوم" else "Today", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("tab_today")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { viewModel.selectTab(1) },
                        icon = { Icon(Icons.Default.History, contentDescription = if (isAr) "السجل" else "Timeline") },
                        label = { Text(if (isAr) "السجل" else "Timeline", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("tab_timeline")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { viewModel.selectTab(2) },
                        icon = { Icon(Icons.Default.Description, contentDescription = if (isAr) "تقرير الطبيب" else "Report") },
                        label = { Text(if (isAr) "التقرير" else "Report", fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("tab_report")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { viewModel.selectTab(3) },
                        icon = { Icon(Icons.Default.MenuBook, contentDescription = if (isAr) "مقياس بريستول" else "Bristol") },
                        label = { Text(if (isAr) "بريستول" else "Bristol", fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("tab_bristol")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 4,
                        onClick = { viewModel.selectTab(4) },
                        icon = { Icon(Icons.Default.Settings, contentDescription = if (isAr) "الإعدادات" else "Settings") },
                        label = { Text(if (isAr) "الإعدادات" else "Settings", fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("tab_settings")
                    )
                }
            }
        },
        floatingActionButton = {
            if (selectedTab == 0 || selectedTab == 1) {
                FloatingActionButton(
                    onClick = { viewModel.openSheet(SheetType.AddBowel) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.testTag("fab_add_bowel")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "إضافة تسجيل")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> TodayHomeScreen(
                    appName = appNameAlias,
                    waterGoalMl = waterGoalMl,
                    lastSuccessfulBowel = lastSuccessfulBowel,
                    bowels = bowels,
                    medications = medications,
                    meals = meals,
                    timeline = timeline,
                    onOpenSheet = { viewModel.openSheet(it) },
                    onQuickWater = { viewModel.quickAddWater(it) },
                    onNavigateToReport = { viewModel.selectTab(2) },
                    onNavigateToTimeline = { viewModel.selectTab(1) },
                    onNavigateToSettings = { viewModel.selectTab(4) },
                    onNavigateToPalettePreview = { showPalettePreviewScreen = true }
                )
                1 -> TimelineHistoryScreen(
                    timelineItems = timeline,
                    onDeleteBowel = { viewModel.deleteBowelEntry(it) },
                    onDeleteMedication = { viewModel.deleteMedication(it) },
                    onDeleteMeal = { viewModel.deleteMeal(it) }
                )
                2 -> DoctorReportScreen(
                    bowels = bowels,
                    medications = medications,
                    meals = meals,
                    onGenerateReportText = { viewModel.getDoctorReportText(it) }
                )
                3 -> BristolReferenceScreen()
                4 -> SettingsScreen(
                    currentThemeMode = themeMode,
                    currentColorPalette = colorPalette,
                    appNameAlias = appNameAlias,
                    appLanguage = appLanguage,
                    waterGoalMl = waterGoalMl,
                    onThemeModeChange = { viewModel.setThemeMode(it) },
                    onColorPaletteChange = { viewModel.setColorPalette(it) },
                    onAppNameAliasChange = { viewModel.setAppNameAlias(it) },
                    onLanguageChange = { viewModel.setAppLanguage(it) },
                    onWaterGoalChange = { viewModel.setWaterGoal(it) },
                    onReseedDemoData = { viewModel.reseedDemoData() },
                    onClearAllData = { viewModel.clearAllData() }
                )
            }
        }

        // Active Bottom Sheets
        when (activeSheet) {
            is SheetType.AddBowel -> {
                AddBowelSheet(
                    onDismiss = { viewModel.closeSheet() },
                    onSave = { viewModel.addBowelEntry(it) }
                )
            }
            is SheetType.AddMedication -> {
                AddMedicationSheet(
                    onDismiss = { viewModel.closeSheet() },
                    onSave = { viewModel.addMedication(it) }
                )
            }
            is SheetType.AddMeal -> {
                AddMealSheet(
                    onDismiss = { viewModel.closeSheet() },
                    onSave = { viewModel.addMeal(it) }
                )
            }
            null -> {}
        }
    }
}
