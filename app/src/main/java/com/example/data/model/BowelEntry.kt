package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bowel_entries")
data class BowelEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val isSuccess: Boolean = true, // true: تم التبرز بنجاح, false: محاولة دون خروج براز (إلحاح كاذب)
    val bristolType: Int = 2, // مقياس بريستول 1 إلى 7
    val strainingLevel: String = "متوسط", // بدون إجهاد، خفيف، متوسط، شديد
    val durationMinutes: Int = 10,
    val incompleteEvacuation: Boolean = false, // شعور بعدم الإفراغ الكامل
    val stoolColor: String = "بني طبيعي",
    val hasBlood: Boolean = false,
    val bloodNotes: String = "",
    val painLevel: Int = 0, // 0 to 10
    val symptoms: String = "", // مثل: مغص، غازات، انتفاخ
    val notes: String = ""
)
