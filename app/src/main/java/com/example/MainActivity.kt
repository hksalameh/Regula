We are given that the original code is empty. Therefore, applying the suggested edit will result in the suggested edit code.
 We are to output the entire suggested edit code block as the modified file.

 However, note that the user's message shows the ORIGINAL CODE as an empty code block and then the SUGGESTED EDIT as a code block.

 Since the original code is empty, the modified file is exactly the suggested edit.

 But note: the problem says "Output the complete modified file."

 Therefore, we output the code from the SUGGESTED EDIT block.
 However, note that the suggested edit code block in the user's message starts with:
   package com.example

 and ends with the closing brace of the MainAppContent function and the subsequent when statement.

 We must output exactly that code.

 Let's write the code as provided in the suggested edit.

 Important: We are to output ONLY the code, no explanations.

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
import com.example.ui.UiState
import com.example.ui.ThemeMode
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.screens.BristolReferenceScreen
import com.example.ui.screens.DoctorReportScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TimelineHistoryScreen
import com.example.ui.screens.TodayHomeScreen
import com.example.ui.sheets.AddBowelSheet
import com.example.ui.sheets.AddMedicationSheet
import com.example.ui.sheets.AddMealSheet
import com.example.ui.components.BottomNavBar

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val isDark = when (uiState.themeMode) {
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
            MyApplicationTheme(darkTheme = isDark, palette = uiState.palette) {
                CompositionLocalProvider(LocalLayoutDirection provides if (uiState.isAr) LayoutDirection.Rtl else LayoutDirection.Ltr) {
                    MainAppContent(viewModel, uiState)
                }
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel, uiState: UiState.MainUiState) {
    BackHandler(enabled = uiState.selectedTab == 3) { viewModel.selectTab(4) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (uiState.selectedTab == 3) {
                Surface(color = MaterialTheme.colorScheme.surface) {
                    Row(Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { viewModel.selectTab(4) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = if (uiState.isAr) "رجوع" else "Back")
                        }
                        Text(if (uiState.isAr) "دليل مقياس بريستول" else "Bristol stool chart", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        },
        bottomBar = {
            BottomNavBar(viewModel = viewModel, uiState = uiState)
        },
        floatingActionButton = {
            if (uiState.selectedTab == 1) {
                FloatingActionButton(onClick = { viewModel.openSheet(SheetType.AddBowel) }, containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.testTag("fab_add_bowel")) {
                    Icon(Icons.Default.Add, contentDescription = if (uiState.isAr) "إضافة تسجيل إخراج" else "Record bowel movement")
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when (uiState.selectedTab) {
                0 -> TodayHomeScreen(
                    appName = uiState.alias, waterGoalMl = uiState.waterGoalMl,
                    lastSuccessfulBowel = uiState.lastSuccessfulBowel, bowels = uiState.bowels,
                    medications = uiState.medications, meals = uiState.meals, timeline = uiState.timeline,
                    onOpenSheet = { viewModel.openSheet(it) },
                    onQuickWater = { viewModel.quickAddWater(it) },
                    onNavigateToReport = { viewModel.selectTab(2) },
                    onNavigateToTimeline = { viewModel.selectTab(1) },
                    onNavigateToSettings = { viewModel.selectTab(4) },
                    isAr = uiState.isAr
                )
                1 -> TimelineHistoryScreen(
                    timelineItems = uiState.timeline,
                    onDeleteBowel = { viewModel.deleteBowelEntry(it) },
                    onDeleteMedication = { viewModel.deleteMedication(it) },
                    onDeleteMeal = { viewModel.deleteMeal(it) }
                )
                2 -> DoctorReportScreen(bowels = uiState.bowels, medications = uiState.medications, meals = uiState.meals, onGenerateReportText = { viewModel.getDoctorReportText(it) })
                3 -> BristolReferenceScreen()
                4 -> SettingsScreen(
                    currentThemeMode = uiState.themeMode, currentColorPalette = uiState.palette,
                    appNameAlias = uiState.alias, appLanguage = uiState.language, waterGoalMl = uiState.waterGoalMl,
                    onThemeModeChange = { viewModel.setThemeMode(it) },
                    onColorPaletteChange = { viewModel.setColorPalette(it) },
                    onAppNameAliasChange = { viewModel.setAppNameAlias(it) },
                    onLanguageChange = { viewModel.setAppLanguage(it) },
                    onWaterGoalChange = { viewModel.setWaterGoal(it) },
                    onReseedDemoData = { viewModel.reseedDemoData() },
                    onClearAllData = { viewModel.clearAllData() },
                    hasRecords = uiState.bowels.isNotEmpty() || uiState.medications.isNotEmpty() || uiState.meals.isNotEmpty(),
                    onNavigateToBristol = { viewModel.selectTab(3) }
                )
            }
        }
        when (uiState.activeSheet) {
            is SheetType.AddBowel -> AddBowelSheet(onDismiss = { viewModel.closeSheet() }, onSave = { viewModel.addBowelEntry(it) })
            is SheetType.AddMedication -> AddMedicationSheet(onDismiss = { viewModel.closeSheet() }, onSave = { viewModel.addMedication(it) })
            is SheetType.AddMeal -> AddMealSheet(onDismiss = { viewModel.closeSheet() }, onSave = { viewModel.addMeal(it) })
            null -> {}
        }
    }
}

