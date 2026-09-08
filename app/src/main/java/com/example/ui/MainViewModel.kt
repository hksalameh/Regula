package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.BowelEntry
import com.example.data.model.MealEntry
import com.example.data.model.MedicationEntry
import com.example.data.model.TimelineItem
import com.example.data.repository.ConstipationRepository
import com.example.ui.theme.AppColorPalette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("constipation_tracker_prefs", Context.MODE_PRIVATE)

    private val database = AppDatabase.getDatabase(application)
    private val repository = ConstipationRepository(
        database.bowelDao(),
        database.medicationDao(),
        database.mealDao()
    )

    // Theme Mode: Default to LIGHT as explicitly requested by user ("اللون الطبيعي")
    private val _themeMode = MutableStateFlow(
        when (prefs.getString("theme_mode", "LIGHT")) {
            "DARK" -> ThemeMode.DARK
            "SYSTEM" -> ThemeMode.SYSTEM
            else -> ThemeMode.LIGHT
        }
    )
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    // Language preference: "AR" or "EN"
    private val _appLanguage = MutableStateFlow(prefs.getString("app_language", "AR") ?: "AR")
    val appLanguage: StateFlow<String> = _appLanguage.asStateFlow()

    // Color Palette Theme - default to NATURAL_TONES
    private val _colorPalette = MutableStateFlow(
        when (prefs.getString("color_palette", "NATURAL_TONES")) {
            "ROYAL_NAVY" -> AppColorPalette.ROYAL_NAVY
            "OCEANIC_TEAL" -> AppColorPalette.OCEANIC_TEAL
            "SAPPHIRE_GOLD" -> AppColorPalette.SAPPHIRE_GOLD
            "CLASSIC_INDIGO" -> AppColorPalette.CLASSIC_INDIGO
            else -> AppColorPalette.NATURAL_TONES
        }
    )
    val colorPalette: StateFlow<AppColorPalette> = _colorPalette.asStateFlow()

    // App display name preference
    private val _appNameAlias = MutableStateFlow(prefs.getString("app_name_alias", "ريغولا") ?: "ريغولا")
    val appNameAlias: StateFlow<String> = _appNameAlias.asStateFlow()

    // Daily water goal in ml (default 2000 ml)
    private val _waterGoalMl = MutableStateFlow(prefs.getInt("water_goal_ml", 2000))
    val waterGoalMl: StateFlow<Int> = _waterGoalMl.asStateFlow()

    val bowelEntries: StateFlow<List<BowelEntry>> = repository.allBowelEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val medicationEntries: StateFlow<List<MedicationEntry>> = repository.allMedications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mealEntries: StateFlow<List<MealEntry>> = repository.allMeals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unifiedTimeline: StateFlow<List<TimelineItem>> = repository.unifiedTimeline
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lastSuccessfulBowel: StateFlow<BowelEntry?> = repository.lastSuccessfulBowel
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Current report filter (7, 14, 30, or 0 for All)
    private val _reportFilterDays = MutableStateFlow(7)
    val reportFilterDays: StateFlow<Int> = _reportFilterDays.asStateFlow()

    // Active bottom navigation destination: 0 = Today, 1 = Timeline, 2 = Doctor Report, 3 = Bristol Guide, 4 = Settings
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    // Sheet states
    private val _activeSheet = MutableStateFlow<SheetType?>(null)
    val activeSheet: StateFlow<SheetType?> = _activeSheet.asStateFlow()

    init {
        // Automatically check if database is empty on first launch and populate demo data
        viewModelScope.launch {
            repository.allBowelEntries.collect { list ->
                if (list.isEmpty()) {
                    repository.seedSampleDataIfEmpty()
                }
            }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        prefs.edit().putString("theme_mode", mode.name).apply()
    }

    fun setAppLanguage(lang: String) {
        _appLanguage.value = lang
        prefs.edit().putString("app_language", lang).apply()
        if (lang == "EN" && (_appNameAlias.value == "ريغولا" || _appNameAlias.value == "سَلاسة")) {
            setAppNameAlias("Regula")
        } else if (lang == "AR" && _appNameAlias.value == "Regula") {
            setAppNameAlias("ريغولا")
        }
    }

    fun setColorPalette(palette: AppColorPalette) {
        _colorPalette.value = palette
        prefs.edit().putString("color_palette", palette.name).apply()
    }

    fun setAppNameAlias(alias: String) {
        _appNameAlias.value = alias
        prefs.edit().putString("app_name_alias", alias).apply()
    }

    fun setWaterGoal(ml: Int) {
        _waterGoalMl.value = ml
        prefs.edit().putInt("water_goal_ml", ml).apply()
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun openSheet(type: SheetType) {
        _activeSheet.value = type
    }

    fun closeSheet() {
        _activeSheet.value = null
    }

    fun setReportFilterDays(days: Int) {
        _reportFilterDays.value = days
    }

    fun addBowelEntry(entry: BowelEntry) {
        viewModelScope.launch {
            repository.insertBowel(entry)
            closeSheet()
        }
    }

    fun deleteBowelEntry(id: Long) {
        viewModelScope.launch {
            repository.deleteBowelById(id)
        }
    }

    fun addMedication(entry: MedicationEntry) {
        viewModelScope.launch {
            repository.insertMedication(entry)
            closeSheet()
        }
    }

    fun deleteMedication(id: Long) {
        viewModelScope.launch {
            repository.deleteMedicationById(id)
        }
    }

    fun addMeal(entry: MealEntry) {
        viewModelScope.launch {
            repository.insertMeal(entry)
            closeSheet()
        }
    }

    fun deleteMeal(id: Long) {
        viewModelScope.launch {
            repository.deleteMealById(id)
        }
    }

    fun quickAddWater(ml: Int = 250) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            repository.insertMeal(
                MealEntry(
                    timestamp = now,
                    mealType = "سوائل وماء",
                    foodsDescription = "شرب ماء وسوائل لترطيب الأمعاء",
                    fiberLevel = "غير محدد",
                    waterMl = ml,
                    triggersSuspected = "",
                    notes = "+$ml مل ماء"
                )
            )
        }
    }

    fun reseedDemoData() {
        viewModelScope.launch {
            // Delete all and re-seed
            val bowels = bowelEntries.value
            val meds = medicationEntries.value
            val meals = mealEntries.value
            bowels.forEach { repository.deleteBowelById(it.id) }
            meds.forEach { repository.deleteMedicationById(it.id) }
            meals.forEach { repository.deleteMealById(it.id) }
            repository.seedSampleDataIfEmpty()
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            val bowels = bowelEntries.value
            val meds = medicationEntries.value
            val meals = mealEntries.value
            bowels.forEach { repository.deleteBowelById(it.id) }
            meds.forEach { repository.deleteMedicationById(it.id) }
            meals.forEach { repository.deleteMealById(it.id) }
        }
    }

    fun getDoctorReportText(days: Int): String {
        val cutoff = if (days > 0) System.currentTimeMillis() - (days.toLong() * 24 * 3600 * 1000L) else 0L
        val filteredBowels = bowelEntries.value.filter { it.timestamp >= cutoff }
        val filteredMeds = medicationEntries.value.filter { it.timestamp >= cutoff }
        val filteredMeals = mealEntries.value.filter { it.timestamp >= cutoff }

        return repository.buildDoctorReport(filteredBowels, filteredMeds, filteredMeals, days)
    }
}

sealed class SheetType {
    object AddBowel : SheetType()
    object AddMedication : SheetType()
    object AddMeal : SheetType()
}
