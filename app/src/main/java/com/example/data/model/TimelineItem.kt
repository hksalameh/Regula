package com.example.data.model

sealed class TimelineItem {
    abstract val id: Long
    abstract val timestamp: Long

    data class Bowel(val entry: BowelEntry) : TimelineItem() {
        override val id: Long = entry.id
        override val timestamp: Long = entry.timestamp
    }

    data class Medication(val entry: MedicationEntry) : TimelineItem() {
        override val id: Long = entry.id
        override val timestamp: Long = entry.timestamp
    }

    data class Meal(val entry: MealEntry) : TimelineItem() {
        override val id: Long = entry.id
        override val timestamp: Long = entry.timestamp
    }
}
