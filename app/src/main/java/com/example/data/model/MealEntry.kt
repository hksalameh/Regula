package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_entries")
data class MealEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val mealType: String = "غداء", // إفطار، غداء، عشاء، وجبة خفيفة / سناك، ماء وسوائل فقط
    val foodsDescription: String = "", // الأطعمة والمشروبات
    val fiberLevel: String = "متوسط الألياف", // غني بالألياف، متوسط الألياف، قليل الألياف
    val waterMl: Int = 250, // كمية السوائل/الماء بالمل
    val triggersSuspected: String = "", // أطعمة مسببة للإمساك تم تناولها (دسم، معجنات بيضاء، جبن، أرز، إلخ)
    val notes: String = ""
)
