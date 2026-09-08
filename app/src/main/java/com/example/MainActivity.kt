package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.SheetType
import com.example.ui.ThemeMode
import com.example.ui.screens.BristolReferenceScreen
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
            val palette by viewModel.colorPalette.collectAsStateWithLifecycle()
            val language by viewModel.appLanguage.collectAsStateWithLifecycle()
            val isDark = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            SideEffect {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !isDark
                    isAppearanceLightNavigationBars = !isDark
                }
            }
            MyApplicationTheme(darkTheme = isDark, palette = palette) {
                CompositionLocalProvider(LocalLayoutDirection provides if (language == "AR") LayoutDirection.Rtl else LayoutDirection.Ltr) {
                    MainAppContent(viewModel)
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
    val palette by viewModel.colorPalette.collectAsStateWithLifecycle()
    val alias by viewModel.appNameAlias.collectAsStateWithLifecycle()
    val language by viewModel.appLanguage.collectAsStateWithLifecycle()
    val waterGoal by viewModel.waterGoalMl.collectAsStateWithLifecycle()
    val isAr = language == "AR"
    val bowels by viewModel.bowelEntries.collectAsStateWithLifecycle()
    val medications by viewModel.medicationEntries.collectAsStateWithLifecycle()
    val meals by viewModel.mealEntries.collectAsStateWithLifecycle()
    val timeline by viewModel.unifiedTimeline.collectAsStateWithLifecycle()
    val lastSuccessfulBowel by viewModel.lastSuccessfulBowel.collectAsStateWithLifecycle()

    BackHandler(enabled = selectedTab == 3) { viewModel.selectTab(4) }
    val destinations = listOf(
        Triple(0, if (isAr) "اليوم" else "Today", Icons.Default.Today),
        Triple(1, if (isAr) "السجل" else "History", Icons.Default.History),
        Triple(2, if (isAr) "التقرير" else "Report", Icons.Default.Description),
        Triple(4, if (isAr) "الإعدادات" else "Settings", Icons.Default.Settings)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (selectedTab == 3) {
                Surface(color = MaterialTheme.colorScheme.surface) {
                    Row(Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { viewModel.selectTab(4) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = if (isAr) "رجوع" else "Back")
                        }
                        Text(if (isAr) "دليل مقياس بريستول" else "Bristol stool chart", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        },
        bottomBar = {
            if (selectedTab != 3) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface, modifier = Modifier.testTag("bottom_nav_bar")) {
                    destinations.forEach { (index, title, icon) ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { viewModel.selectTab(index) },
                            icon = { Icon(icon, contentDescription = null) },
                            label = { Text(title, fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal) },
                            modifier = Modifier.testTag(when (index) { 0 -> "tab_today"; 1 -> "tab_timeline"; 2 -> "tab_report"; else -> "tab_settings" })
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (selectedTab == 1) {
                FloatingActionButton(onClick = { viewModel.openSheet(SheetType.AddBowel) }, containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.testTag("fab_add_bowel")) {
                    Icon(Icons.Default.Add, contentDescription = if (isAr) "إضافة تسجيل إخراج" else "Record bowel movement")
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when (selectedTab) {
                0 -> TodayHomeScreen(
                    appName = alias, waterGoalMl = waterGoal,
                    lastSuccessfulBowel = lastSuccessfulBowel, bowels = bowels,
                    medications = medications, meals = meals, timeline = timeline,
                    onOpenSheet = { viewModel.openSheet(it) },
                    onQuickWater = { viewModel.quickAddWater(it) },
                    onNavigateToReport = { viewModel.selectTab(2) },
                    onNavigateToTimeline = { viewModel.selectTab(1) },
                    onNavigateToSettings = { viewModel.selectTab(4) },
                    isAr = isAr
                )
                1 -> TimelineHistoryScreen(
                    timelineItems = timeline,
                    onDeleteBowel = { viewModel.deleteBowelEntry(it) },
                    onDeleteMedication = { viewModel.deleteMedication(it) },
                    onDeleteMeal = { viewModel.deleteMeal(it) }
                )
                2 -> DoctorReportScreen(bowels = bowels, medications = medications, meals = meals, onGenerateReportText = { viewModel.getDoctorReportText(it) })
                3 -> BristolReferenceScreen()
                4 -> SettingsScreen(
                    currentThemeMode = themeMode, currentColorPalette = palette,
                    appNameAlias = alias, appLanguage = language, waterGoalMl = waterGoal,
                    onThemeModeChange = { viewModel.setThemeMode(it) },
                    onColorPaletteChange = { viewModel.setColorPalette(it) },
                    onAppNameAliasChange = { viewModel.setAppNameAlias(it) },
                    onLanguageChange = { viewModel.setAppLanguage(it) },
                    onWaterGoalChange = { viewModel.setWaterGoal(it) },
                    onReseedDemoData = { viewModel.reseedDemoData() },
                    onClearAllData = { viewModel.clearAllData() },
                    hasRecords = bowels.isNotEmpty() || medications.isNotEmpty() || meals.isNotEmpty(),
                    onNavigateToBristol = { viewModel.selectTab(3) }
                )
            }
        }
        when (activeSheet) {
            is SheetType.AddBowel -> AddBowelSheet(onDismiss = { viewModel.closeSheet() }, onSave = { viewModel.addBowelEntry(it) })
            is SheetType.AddMedication -> AddMedicationSheet(onDismiss = { viewModel.closeSheet() }, onSave = { viewModel.addMedication(it) })
            is SheetType.AddMeal -> AddMealSheet(onDismiss = { viewModel.closeSheet() }, onSave = { viewModel.addMeal(it) })
            null -> {}
        }
    }
}
