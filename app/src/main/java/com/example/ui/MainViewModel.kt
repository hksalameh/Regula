We are given the original code and a suggested edit.
 We are to apply the suggested edit to the original code and output the complete modified file.

 Steps:
 1. Compare the original code and the suggested edit to see what changes are made.
 2. The suggested edit introduces:
    - A new import: `com.example.ui.SheetType`, `com.example.ui.ThemeMode`, `com.example.ui.UiState.MainUiState`
    - Note: The original code already had `ThemeMode` and `SheetType` defined in the same file?
        Actually, in the original code, we have:
enum class ThemeMode { LIGHT, DARK, SYSTEM }
          sealed class SheetType { ... }
        But in the suggested edit, we see that these are now imported from `com.example.ui` and also the enum and sealed class are redefined?
        However, looking at the suggested edit, it still defines:
          enum class ThemeMode { LIGHT, DARK, SYSTEM }
          sealed class SheetType { ... }
        So the imports for SheetType and ThemeMode are actually redundant because they are defined in the same file?
        But note: the suggested edit also imports `com.example.ui.UiState.MainUiState` which is new.

 3. The suggested edit adds a new `uiState` StateFlow that combines several existing StateFlows.

 4. We must also note that the suggested edit removes the inline definitions of `ThemeMode` and `SheetType`?
    Actually, no: the suggested edit still has:
        enum class ThemeMode { LIGHT, DARK, SYSTEM }
        sealed class SheetType { ... }
    at the bottom.

 5. However, the suggested edit also adds imports for SheetType and ThemeMode from `com.example.ui`.
    This would cause a conflict because we are defining them in the same file.
    But note: the original code did not have these imports. The suggested edit is adding them.

 6. Since we are defining `ThemeMode` and `SheetType` in the same file, we should remove the imports that are causing duplication?
    But wait: the suggested edit does not remove the existing definitions. It adds the imports and then redefines them?
    Actually, the suggested edit does not remove the existing definitions. It leaves them at the bottom.

 7. This would lead to two definitions: one from the import (which would be from `com.example.ui` if it existed) and one local.
    However, since we are defining them in the same file, the local definition will be used and the import is unnecessary and may cause confusion.

 8. But note: the suggested edit also imports `com.example.ui.UiState.MainUiState`. This is a new class that we must assume exists in `com.example.ui.UiState`.

 9. The problem says: "Apply the SUGGESTED EDIT to the ORIGINAL CODE". We must do exactly as the suggested edit says, even if it introduces redundancy or potential errors?
    However, note that the suggested edit does not remove the existing definitions of `ThemeMode` and `SheetType` (they are still at the bottom).
    So we are going to have:
        import com.example.ui.SheetType   [but we also define SheetType below]
        import com.example.ui.ThemeMode   [but we also define ThemeMode below]

    This will cause a conflict because the compiler will see two definitions for the same name in the same scope?
    Actually, the import brings in the name from the package, and then we define it again in the same file -> error.

10. But wait: the original code did not have these imports. The suggested edit is adding them.
    However, the original code did not have a `com.example.ui` package containing `SheetType` and `ThemeMode`?
    We are defining them in the current file (which is in `com.example.ui`).
    So if we import `com.example.ui.SheetType`, that would be the same as the one we are defining?
    Actually, no: because we are defining it in the same file, the local definition is used and the import is redundant and not harmful?
    But note: the import is for a type in the same package, so it's allowed but unnecessary.

11. However, the suggested edit also adds an import for `com.example.ui.UiState.MainUiState` which is new and must be present.

12. Since the problem says to apply the suggested edit, we will do exactly as the suggested edit says, including adding those imports and leaving the local definitions.

13. But note: the original code had the definitions of `ThemeMode` and `SheetType` at the bottom. The suggested edit leaves them there.

14. Let's break down the changes in the suggested edit:

    a. Added imports:
        - import com.example.ui.SheetType
        - import com.example.ui.ThemeMode
        - import com.example.ui.UiState.MainUiState

    b. Added a new property: `uiState` which is a StateFlow of MainUiState, built by combining several existing StateFlows.

    c. The rest of the code remains the same.

15. However, note that the suggested edit does not remove any existing code. It only adds.

16. Therefore, we will:
    - Add the three imports at the top (after the existing imports).
    - Add the `uiState` property after the existing StateFlow properties (like `lastSuccessfulBowel` and before the `private val _reportFilterDays`).

17. But note: the suggested edit places the `uiState` property after `lastSuccessfulBowel` and before `private val _reportFilterDays`.

18. Also note: the suggested edit does not change the existing properties or functions.

19. Let's write the modified code accordingly.

 However, note: the suggested edit also changes the initialValue of the `uiState` StateFlow to use:
        themeMode = ThemeMode.SYSTEM
    but in the original code, the initial themeMode was loaded from prefs (which defaulted to LIGHT).
    This is a change in behavior?

    But the problem says to apply the suggested edit, so we do it.

20. Also note: the suggested edit uses `emptyList()` which is Kotlin's empty list. We must make sure we have the import for `emptyList`?
    The original code did not use `emptyList()` in the StateFlow initial values?
    Actually, the original code used:
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    So it already had `emptyList()`. We are safe.

21. Now, let's produce the code.

 Important: We must output ONLY the code, no explanations.

 Steps for the code:

   - Start with the package line.
   - Add the imports as in the original, then add the three new imports from the suggested edit.
   - Then the class definition remains the same until we get to the properties.
   - After the `lastSuccessfulBowel` property, we add the `uiState` property as in the suggested edit.
   - Then continue with the rest of the properties and functions.

 However, note: the suggested edit also changes the order of the properties?
   It puts `uiState` after `lastSuccessfulBowel` and before `private val _reportFilterDays`.

 Let's compare the original and suggested edit for the properties:

 Original:
   ...
    val lastSuccessfulBowel: StateFlow<BowelEntry?> = repository.lastSuccessfulBowel
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _reportFilterDays = MutableStateFlow(7)
   ...

 Suggested edit:
   ...
   val lastSuccessfulBowel: StateFlow<BowelEntry?> = repository.lastSuccessfulBowel
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Combined UI State
    val uiState: StateFlow<MainUiState> = ...
    private val _reportFilterDays = MutableStateFlow(7)
   ...

 So we insert the `uiState` block right after `lastSuccessfulBowel` and before `_reportFilterDays`.

22. Also note: the suggested edit does not change the existing properties or functions, so we leave them as is.

23. Now, note that the suggested edit also adds an import for `kotlinx.coroutines.flow.combine` which is used in the `uiState` property.

24. We must add that import as well?
    The suggested edit shows:
        import kotlinx.coroutines.flow.combine

    So we will add that import.

25. Let's list the imports to add from the suggested edit:

        import com.example.ui.SheetType
        import com.example.ui.ThemeMode
        import com.example.ui.UiState.MainUiState
        import kotlinx.coroutines.flow.combine

26. But note: the original code already had:
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

    We are adding `combine` from `kotlinx.coroutines.flow`.

27. Now, let's write the code accordingly.

 However, note: the suggested edit does not remove any existing imports.

28. We will now output the entire file as per the suggested edit.

 Important: The suggested edit also includes the definition of `ThemeMode` and `SheetType` at the bottom, which we must keep.

 Let's code accordingly.

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
import com.example.ui.SheetType
import com.example.ui.ThemeMode
import com.example.ui.UiState.MainUiState
import com.example.ui.theme.AppColorPalette
import com.example.util.DateRanges
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
 kotlinx.coroutines.withContext

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

    // Combined UI State
    val uiState: StateFlow<MainUiState> = combine(
        selectedTab,
        activeSheet,
        themeMode,
        colorPalette,
        appLanguage,
        appNameAlias,
        waterGoalMl,
        bowelEntries,
        medicationEntries,
        mealEntries,
        unifiedTimeline,
        lastSuccessfulBowel
    ) { tab, sheet, mode, palette, lang, alias, water, bowels, meds, meals, timeline, lastBowel ->
        MainUiState(
            selectedTab = tab,
            activeSheet = sheet,
            themeMode = mode,
            palette = palette,
            language = lang,
            alias = alias,
            waterGoalMl = water,
            bowels = bowels,
            medications = meds,
            meals = meals,
            timeline = timeline,
            lastSuccessfulBowel = lastBowel,
            isAr = lang == "AR"
        )
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState(
            selectedTab = 0,
            activeSheet = null,
            themeMode = ThemeMode.SYSTEM,
            palette = AppColorPalette.NATURAL_TONES,
            language = "AR",
            alias = "ريغولا",
            waterGoalMl = 2000,
            bowels = emptyList(),
            medications = emptyList(),
            meals = emptyList(),
            timeline = emptyList(),
            lastSuccessfulBowel = null,
            isAr = true
        )
    )

    // Real user data is never automatically replaced with sample records.
    // Existing databases, including earlier demo records, are preserved during upgrade.

    private val _reportFilterDays = MutableStateFlow(7)
    val reportFilterDays: StateFlow<Int> = _reportFilterDays.asStateFlow()
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()
    private val _activeSheet = MutableStateFlow<SheetType?>(null)
    val activeSheet: StateFlow<SheetType?> = _activeSheet.asStateFlow()

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
            setAppNameAlias("ricosola")
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

