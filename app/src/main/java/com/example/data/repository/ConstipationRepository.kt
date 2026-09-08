package com.example.data.repository

import com.example.data.local.BowelDao
import com.example.data.local.MealDao
import com.example.data.local.MedicationDao
import com.example.data.model.BowelEntry
import com.example.data.model.MealEntry
import com.example.data.model.MedicationEntry
import com.example.data.model.TimelineItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConstipationRepository(
    private val bowelDao: BowelDao,
    private val medicationDao: MedicationDao,
    private val mealDao: MealDao
) {
    val allBowelEntries: Flow<List<BowelEntry>> = bowelDao.getAllBowelEntries()
    val allMedications: Flow<List<MedicationEntry>> = medicationDao.getAllMedications()
    val allMeals: Flow<List<MealEntry>> = mealDao.getAllMeals()
    val lastSuccessfulBowel: Flow<BowelEntry?> = bowelDao.getLastSuccessfulBowel()

    // Unified chronological timeline
    val unifiedTimeline: Flow<List<TimelineItem>> = combine(
        bowelDao.getAllBowelEntries(),
        medicationDao.getAllMedications(),
        mealDao.getAllMeals()
    ) { bowels, meds, meals ->
        val list = mutableListOf<TimelineItem>()
        bowels.forEach { list.add(TimelineItem.Bowel(it)) }
        meds.forEach { list.add(TimelineItem.Medication(it)) }
        meals.forEach { list.add(TimelineItem.Meal(it)) }
        list.sortedByDescending { it.timestamp }
    }

    suspend fun insertBowel(entry: BowelEntry) = bowelDao.insertBowelEntry(entry)
    suspend fun updateBowel(entry: BowelEntry) = bowelDao.updateBowelEntry(entry)
    suspend fun deleteBowelById(id: Long) = bowelDao.deleteBowelEntryById(id)

    suspend fun insertMedication(entry: MedicationEntry) = medicationDao.insertMedication(entry)
    suspend fun updateMedication(entry: MedicationEntry) = medicationDao.updateMedication(entry)
    suspend fun deleteMedicationById(id: Long) = medicationDao.deleteMedicationById(id)

    suspend fun insertMeal(entry: MealEntry) = mealDao.insertMeal(entry)
    suspend fun updateMeal(entry: MealEntry) = mealDao.updateMeal(entry)
    suspend fun deleteMealById(id: Long) = mealDao.deleteMealById(id)

    // Seed realistic demo data if completely empty so the user can immediately evaluate the app
    suspend fun seedSampleDataIfEmpty() {
        val now = System.currentTimeMillis()
        val day = 24 * 60 * 60 * 1000L

        // Day -3
        insertMeal(
            MealEntry(
                timestamp = now - (3 * day) + (8 * 3600 * 1000L),
                mealType = "إفطار",
                foodsDescription = "بيض مقلي وخبز أبيض مع شاي ثقيل",
                fiberLevel = "قليل الألياف",
                waterMl = 250,
                triggersSuspected = "خبز أبيض، شاي ثقيل"
            )
        )
        insertMeal(
            MealEntry(
                timestamp = now - (3 * day) + (14 * 3600 * 1000L),
                mealType = "غداء",
                foodsDescription = "أرز أبيض مع دجاج وبطاطس مقلية",
                fiberLevel = "قليل الألياف",
                waterMl = 500,
                triggersSuspected = "أرز أبيض ونقص خضروات"
            )
        )
        insertBowel(
            BowelEntry(
                timestamp = now - (3 * day) + (20 * 3600 * 1000L),
                isSuccess = false,
                bristolType = 1,
                strainingLevel = "شديد",
                durationMinutes = 25,
                incompleteEvacuation = true,
                painLevel = 7,
                symptoms = "مغص وانتفاخ شديد، شعور بالامتلاء",
                notes = "محاولة مؤلمة في الحمام دون أي خروج للبراز"
            )
        )

        // Day -2
        insertMedication(
            MedicationEntry(
                timestamp = now - (2 * day) + (9 * 3600 * 1000L),
                name = "موفيكول (PEG 3350)",
                category = "ملين أسموزي",
                dosage = "كيس واحد مذاب في كوب ماء",
                effectObserved = "لم يحدث إخراج فوري",
                notes = "تم شرب الدواء صباحاً مع كوبين ماء"
            )
        )
        insertMeal(
            MealEntry(
                timestamp = now - (2 * day) + (13 * 3600 * 1000L),
                mealType = "غداء",
                foodsDescription = "شوربة خضار مع سلطة خضراء وشوفان",
                fiberLevel = "غني بالألياف",
                waterMl = 750,
                triggersSuspected = ""
            )
        )
        insertMedication(
            MedicationEntry(
                timestamp = now - (2 * day) + (22 * 3600 * 1000L),
                name = "دوفالاك (لاكتولوز)",
                category = "ملين أسموزي",
                dosage = "15 مل",
                effectObserved = "أحدث غازات وقرقرة في البطن",
                notes = "أخذته قبل النوم لتسهيل الإخراج في الصباح"
            )
        )

        // Day -1
        insertBowel(
            BowelEntry(
                timestamp = now - (1 * day) + (7 * 3600 * 1000L),
                isSuccess = true,
                bristolType = 1,
                strainingLevel = "شديد",
                durationMinutes = 20,
                incompleteEvacuation = true,
                stoolColor = "بني داكن",
                hasBlood = true,
                bloodNotes = "بضع قطرات دم فاتح على ورق الحمام بسبب الشد",
                painLevel = 6,
                symptoms = "ألم شرجي وإجهاد شديد",
                notes = "خرجت كتل صغيرة صلبة جداً كحبات الجوز بعد جهد كبير"
            )
        )
        insertMeal(
            MealEntry(
                timestamp = now - (1 * day) + (13 * 3600 * 1000L),
                mealType = "غداء",
                foodsDescription = "صحن عدس مع بذور الكتان وخبز قمح كامل وسلطة",
                fiberLevel = "غني بالألياف",
                waterMl = 1000,
                triggersSuspected = ""
            )
        )
        insertMedication(
            MedicationEntry(
                timestamp = now - (1 * day) + (21 * 3600 * 1000L),
                name = "ألياف بذور القاطونة (Psyllium)",
                category = "مكمل ألياف حجمي",
                dosage = "ملعقة كبيرة في كوب ماء",
                effectObserved = "شعور بالارتياح",
                notes = "مع شرب كمية وافرة من الماء"
            )
        )

        // Today
        insertMeal(
            MealEntry(
                timestamp = now - (4 * 3600 * 1000L),
                mealType = "إفطار",
                foodsDescription = "زبادي مع فواكه مجففة (قراصيا وتين) وبذور الشيا",
                fiberLevel = "غني بالألياف",
                waterMl = 500,
                triggersSuspected = ""
            )
        )
        insertBowel(
            BowelEntry(
                timestamp = now - (1 * 3600 * 1000L),
                isSuccess = true,
                bristolType = 3,
                strainingLevel = "متوسط",
                durationMinutes = 12,
                incompleteEvacuation = false,
                stoolColor = "بني طبيعي",
                hasBlood = false,
                bloodNotes = "",
                painLevel = 2,
                symptoms = "تحسن نسبي بعد شرب الألياف والماء",
                notes = "تبرز أفضل من السابق، سجقي متماسك بتشققات خفيفة"
            )
        )
    }

    /**
     * Generates a comprehensive clinical summary text for the doctor
     */
    fun buildDoctorReport(
        bowels: List<BowelEntry>,
        meds: List<MedicationEntry>,
        meals: List<MealEntry>,
        daysFilter: Int
    ): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale("ar"))
        val timeFormat = SimpleDateFormat("hh:mm a", Locale("ar"))
        val fullDateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale("ar"))

        val totalBowelAttempts = bowels.size
        val successfulBowels = bowels.filter { it.isSuccess }
        val failedAttempts = bowels.filter { !it.isSuccess }
        val severeStrainingCount = bowels.count { it.strainingLevel == "شديد" }
        val incompleteCount = bowels.count { it.incompleteEvacuation }
        val bleedingCount = bowels.count { it.hasBlood }
        val avgBristol = if (successfulBowels.isNotEmpty()) {
            String.format(Locale.US, "%.1f", successfulBowels.map { it.bristolType }.average())
        } else "غير متوفر"

        val avgDuration = if (bowels.isNotEmpty()) {
            bowels.map { it.durationMinutes }.average().toInt()
        } else 0

        val totalWater = meals.sumOf { it.waterMl }
        val daysCount = if (daysFilter > 0) daysFilter else 30
        val avgWaterDaily = if (daysCount > 0) totalWater / daysCount else totalWater

        val sb = StringBuilder()
        sb.append("📋 [تقرير متابعة الإمساك المزمن - موجه للطبيب المعالج]\n")
        sb.append("════════════════════════════════════════\n")
        sb.append("تاريخ استخراج التقرير: ${fullDateFormat.format(Date())}\n")
        sb.append("نطاق التقرير: ${if (daysFilter > 0) "آخر $daysFilter يوم" else "كامل السجل الطبي"}\n\n")

        sb.append("📊 1. ملخص حركات الأمعاء والتبرز:\n")
        sb.append("• إجمالي محاولات دخول الحمام: $totalBowelAttempts مرة\n")
        sb.append("• مرات التبرز الفعلي الناجح: ${successfulBowels.size} مرة\n")
        sb.append("• محاولات الدخول دون خروج براز (إلحاح كاذب): ${failedAttempts.size} مرة\n")
        sb.append("• متوسط مقياس بريستول لقوام البراز: $avgBristol من 7 (النوع 1-2 يمثل إمساكاً)\n")
        sb.append("• حالات الإجهاد الشديد (الحزق المفرط): $severeStrainingCount مرة\n")
        sb.append("• الشعور بعدم الإفراغ الكامل: $incompleteCount مرة\n")
        sb.append("• حالات وجود دم مصاحب: $bleedingCount مرة\n")
        sb.append("• متوسط الوقت المستغرق في الحمام: $avgDuration دقيقة\n\n")

        sb.append("💊 2. الأدوية والملينات المسجلة (${meds.size} جرعة):\n")
        if (meds.isEmpty()) {
            sb.append("• لم يتم تسجيل أدوية أو ملينات في هذه الفترة.\n")
        } else {
            val medGrouped = meds.groupBy { it.name }
            medGrouped.forEach { (name, entries) ->
                sb.append("• $name (${entries.size} مرات) - التصنيف: ${entries.firstOrNull()?.category ?: ""}\n")
                entries.take(3).forEach {
                    sb.append("   - ${dateFormat.format(Date(it.timestamp))}: جرعة [${it.dosage}] | النتيجة: [${it.effectObserved}]\n")
                }
            }
        }
        sb.append("\n")

        sb.append("🥗 3. النظام الغذائي والسوائل:\n")
        sb.append("• عدد الوجبات المسجلة: ${meals.size} وجبة\n")
        sb.append("• معدل شرب الماء المقدر: حوالي $avgWaterDaily مل يومياً\n")
        val highFiberMeals = meals.count { it.fiberLevel == "غني بالألياف" }
        val lowFiberMeals = meals.count { it.fiberLevel == "قليل الألياف" }
        sb.append("• وجبات غنية بالألياف: $highFiberMeals | وجبات فقيرة بالألياف: $lowFiberMeals\n\n")

        sb.append("⏱️ 4. السجل الزمني التفصيلي للأحداث:\n")
        sb.append("────────────────────────────────────────\n")
        val allEvents = mutableListOf<TimelineItem>()
        bowels.forEach { allEvents.add(TimelineItem.Bowel(it)) }
        meds.forEach { allEvents.add(TimelineItem.Medication(it)) }
        meals.forEach { allEvents.add(TimelineItem.Meal(it)) }
        allEvents.sortByDescending { it.timestamp }

        if (allEvents.isEmpty()) {
            sb.append("لا توجد أحداث مسجلة.\n")
        } else {
            allEvents.take(40).forEach { item ->
                when (item) {
                    is TimelineItem.Bowel -> {
                        val b = item.entry
                        val status = if (b.isSuccess) "تبرز ناجح (نوع بريستول: ${b.bristolType})" else "محاولة دخول دون تبرز"
                        sb.append("🚽 [دخول الحمام] ${fullDateFormat.format(Date(b.timestamp))}\n")
                        sb.append("   - الحالة: $status | الجهد: ${b.strainingLevel} | المدة: ${b.durationMinutes} د\n")
                        if (b.hasBlood) sb.append("   - ⚠️ دم: نعم (${b.bloodNotes})\n")
                        if (b.painLevel > 0) sb.append("   - ألم: ${b.painLevel}/10 | أعراض: ${b.symptoms}\n")
                        if (b.notes.isNotBlank()) sb.append("   - ملاحظات: ${b.notes}\n")
                    }
                    is TimelineItem.Medication -> {
                        val m = item.entry
                        sb.append("💊 [دواء/ملين] ${fullDateFormat.format(Date(m.timestamp))}\n")
                        sb.append("   - الدواء: ${m.name} (${m.dosage}) | الأثر: ${m.effectObserved}\n")
                        if (m.notes.isNotBlank()) sb.append("   - ملاحظة: ${m.notes}\n")
                    }
                    is TimelineItem.Meal -> {
                        val meal = item.entry
                        sb.append("🍽️ [وجبة طعام] ${fullDateFormat.format(Date(meal.timestamp))}\n")
                        sb.append("   - النوع: ${meal.mealType} | الألياف: ${meal.fiberLevel} | سوائل: ${meal.waterMl} مل\n")
                        if (meal.foodsDescription.isNotBlank()) sb.append("   - الطعام: ${meal.foodsDescription}\n")
                        if (meal.triggersSuspected.isNotBlank()) sb.append("   - مسببات محتملة: ${meal.triggersSuspected}\n")
                    }
                }
                sb.append("────────────────────\n")
            }
        }

        sb.append("\nتم التوثيق والمتابعة بواسطة تطبيق سجل الإمساك الطبي.")
        return sb.toString()
    }
}
