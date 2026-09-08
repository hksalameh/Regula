package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MedicationEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medication_entries ORDER BY timestamp DESC")
    fun getAllMedications(): Flow<List<MedicationEntry>>

    @Query("SELECT * FROM medication_entries WHERE timestamp >= :sinceTimestamp ORDER BY timestamp DESC")
    fun getMedicationsSince(sinceTimestamp: Long): Flow<List<MedicationEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(entry: MedicationEntry): Long

    @Update
    suspend fun updateMedication(entry: MedicationEntry)

    @Query("DELETE FROM medication_entries WHERE id = :id")
    suspend fun deleteMedicationById(id: Long)
}
