package com.example.nutripal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nutripal.data.local.entity.HistoryItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM nutrition_history WHERE uid = :userId ORDER BY timestamp DESC")
    fun getAllHistoryItems(userId: String): Flow<List<HistoryItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItem(historyItem: HistoryItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllHistoryItems(historyItems: List<HistoryItemEntity>)

    @Query("DELETE FROM nutrition_history WHERE documentId = :documentId")
    suspend fun deleteHistoryItem(documentId: String)

    @Query("SELECT * FROM nutrition_history WHERE documentId = :documentId")
    fun getHistoryItemById(documentId: String): Flow<HistoryItemEntity?>
}
