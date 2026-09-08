package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BowelEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface BowelDao {
    @Query("SELECT * FROM bowel_entries ORDER BY timestamp DESC")
    fun getAllBowelEntries(): Flow<List<BowelEntry>>

    @Query("SELECT * FROM bowel_entries WHERE timestamp >= :sinceTimestamp ORDER BY timestamp DESC")
    fun getBowelEntriesSince(sinceTimestamp: Long): Flow<List<BowelEntry>>

    @Query("SELECT * FROM bowel_entries WHERE isSuccess = 1 ORDER BY timestamp DESC LIMIT 1")
    fun getLastSuccessfulBowel(): Flow<BowelEntry?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBowelEntry(entry: BowelEntry): Long

    @Update
    suspend fun updateBowelEntry(entry: BowelEntry)

    @Query("DELETE FROM bowel_entries WHERE id = :id")
    suspend fun deleteBowelEntryById(id: Long)
}
