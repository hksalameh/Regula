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
import com.example.util.DateRanges
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

enum class ThemeMode { LIGHT, DARK, SYSTEM }

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("constipation_tracker_prefs", Context.MODE_PRIVATE)
    private val database = AppDatabase.getDatabase(application)
    private val repository = ConstipationRepository(database.bowelDao(), database.medicationDao(), database.mealDao())
    private val dataOperationMutex = Mutex()

    private val _themeMode = MutableStateFlow(
        when (prefs.getString("theme_mode", "LIGHT")) {
            "DARK" -> ThemeMode.DARK
            "SYSTEM" -> ThemeMode.SYSTEM
            else -> ThemeMode.LIGHT
        }
    )
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _appLanguage = MutableStateFlow(prefs.getString("app_language", "AR") ?: "AR")
    val appLanguage: StateFlow<String> = _appLanguage.asStateFlow()

    private val _colorPalette = MutableStateFlow(
        AppColorPalette.entries.firstOrNull { it.name == prefs.getString("color_palette", "NATURAL_TONES") }
            ?: AppColorPalette.NATURAL_TONES
    )
    val colorPalette: StateFlow<AppColorPalette> = _colorPalette.asStateFlow()

    private val _appNameAlias = MutableStateFlow(prefs.getString("app_name_alias", "ريغولا") ?: "ريغولا")
    val appNameAlias: StateFlow<String> = _appNameAlias.asStateFlow()

    private val _waterGoalMl = MutableStateFlow(prefs.getInt("water_goal_ml", 2000).coerceIn(250, 10000))
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

    private val _reportFilterDays = MutableStateFlow(7)
    val reportFilterDays: StateFlow<Int> = _reportFilterDays.asStateFlow()
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()
    private val _activeSheet = MutableStateFlow<SheetType?>(null)
    val activeSheet: StateFlow<SheetType?> = _activeSheet.asStateFlow()

    // Real user data is never automatically replaced with sample records.
    // Existing databases, including earlier demo records, are preserved during upgrade.

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        prefs.edit().putString("theme_mode", mode.name).apply()
    }

    fun setAppLanguage(lang: String) {
        if (lang != "AR" && lang != "EN") return
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
        _waterGoalMl.value = ml.coerceIn(250, 10000)
        prefs.edit().putInt("water_goal_ml", _waterGoalMl.value).apply()
    }

    fun selectTab(index: Int) { _selectedTab.value = index }
    fun openSheet(type: SheetType) { _activeSheet.value = type }
    fun closeSheet() { _activeSheet.value = null }
    fun setReportFilterDays(days: Int) { _reportFilterDays.value = days }

    fun addBowelEntry(entry: BowelEntry) {
        viewModelScope.launch { repository.insertBowel(entry); closeSheet() }
    }
    fun deleteBowelEntry(id: Long) {
        viewModelScope.launch { repository.deleteBowelById(id) }
    }
    fun addMedication(entry: MedicationEntry) {
        viewModelScope.launch { repository.insertMedication(entry); closeSheet() }
    }
    fun deleteMedication(id: Long) {
        viewModelScope.launch { repository.deleteMedicationById(id) }
    }
    fun addMeal(entry: MealEntry) {
        viewModelScope.launch { repository.insertMeal(entry); closeSheet() }
    }
    fun deleteMeal(id: Long) {
        viewModelScope.launch { repository.deleteMealById(id) }
    }

    fun quickAddWater(ml: Int = 250) {
        viewModelScope.launch {
            repository.insertMeal(
                MealEntry(
                    timestamp = System.currentTimeMillis(),
                    mealType = "ماء وسوائل",
                    foodsDescription = "",
                    fiberLevel = "غير محدد",
                    waterMl = ml.coerceAtLeast(0),
                    triggersSuspected = "",
                    notes = "إضافة سريعة للماء"
                )
            )
        }
    }

    /** Explicit demo action only. Never deletes or overwrites existing records. */
    fun reseedDemoData() {
        viewModelScope.launch {
            dataOperationMutex.withLock {
                val empty = repository.allBowelEntries.first().isEmpty() &&
                    repository.allMedications.first().isEmpty() && repository.allMeals.first().isEmpty()
                if (empty) repository.seedSampleDataIfEmpty()
            }
        }
    }

    /** Room clears every table in one transaction; no stale StateFlow snapshot is used. */
    fun clearAllData() {
        viewModelScope.launch {
            dataOperationMutex.withLock {
                withContext(Dispatchers.IO) { database.clearAllTables() }
            }
        }
    }

    fun getDoctorReportText(days: Int): String {
        val cutoff = if (days > 0) DateRanges.startOfLookback(days) else 0L
        return repository.buildDoctorReport(
            bowelEntries.value.filter { it.timestamp >= cutoff },
            medicationEntries.value.filter { it.timestamp >= cutoff },
            mealEntries.value.filter { it.timestamp >= cutoff },
            days
        )
    }
}

sealed class SheetType {
    object AddBowel : SheetType()
    object AddMedication : SheetType()
    object AddMeal : SheetType()
}
