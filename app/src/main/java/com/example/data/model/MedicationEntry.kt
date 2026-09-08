package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication_entries")
data class MedicationEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val name: String, // اسم الدواء أو الملين
    val category: String = "ملين", // ملين أسموزي، ملين منبه، مكمل ألياف، مطري براز، تحميلة/حقنة شرجية، أدوية أخرى
    val dosage: String = "", // الجرعة (مثال: كيس واحد، 15 مل، قرصين)
    val effectObserved: String = "قيد المراقبة", // أحدث إخراج، إخراج جزئي، لم يحدث تأثير، سبب مغص
    val notes: String = ""
)
