package com.dabai.app.data.local.dao

import androidx.room.*
import com.dabai.app.data.local.entity.HealthRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthRecordDao {
    @Query("SELECT * FROM health_records WHERE record_id = :recordId")
    suspend fun getRecordById(recordId: String): HealthRecordEntity?

    @Query("SELECT * FROM health_records WHERE person_id = :personId ORDER BY analyzed_at DESC")
    fun observeRecordsByPersonId(personId: String): Flow<List<HealthRecordEntity>>

    @Query("SELECT * FROM health_records WHERE person_id = :personId ORDER BY analyzed_at DESC LIMIT 1")
    suspend fun getLatestRecord(personId: String): HealthRecordEntity?

    @Query("SELECT * FROM health_records WHERE person_id = :personId ORDER BY analyzed_at DESC LIMIT 1 OFFSET 1")
    suspend fun getSecondLatestRecord(personId: String): HealthRecordEntity?

    @Query("SELECT * FROM health_records WHERE person_id = :personId ORDER BY analyzed_at DESC")
    suspend fun getRecordsByPersonId(personId: String): List<HealthRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: HealthRecordEntity)

    @Update
    suspend fun updateRecord(record: HealthRecordEntity)

    @Delete
    suspend fun deleteRecord(record: HealthRecordEntity)
}
