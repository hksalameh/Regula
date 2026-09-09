package com.example.ui

import com.example.ui.SheetType
import com.example.ui.ThemeMode
import com.example.ui.theme.AppColorPalette

/**
 * Data class that holds all the UI state needed for the MainAppContent.
 * This prevents collecting multiple states separately and reduces recompositions.
 */
data class MainUiState(
    val selectedTab: Int,
    val activeSheet: SheetType?,
    val themeMode: ThemeMode,
    val palette: AppColorPalette,
    val language: String,
    val alias: String,
    val waterGoalMl: Int,
    val bowels: List<BowelEntry>,
    val medications: List<MedicationEntry>,
    val meals: List<MealEntry>,
    val timeline: List<TimelineItem>,
    val lastSuccessfulBowel: BowelEntry?,
    val isAr: Boolean
)