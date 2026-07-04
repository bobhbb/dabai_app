package com.dabai.app.data.local.dao

import androidx.room.*
import com.dabai.app.data.local.entity.FaceDataEntity

@Dao
interface FaceDataDao {
    @Query("SELECT * FROM face_data WHERE face_id = :faceId")
    suspend fun getFaceDataById(faceId: String): FaceDataEntity?

    @Query("SELECT * FROM face_data WHERE person_id = :personId ORDER BY created_at DESC")
    suspend fun getFaceDataByPersonId(personId: String): List<FaceDataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaceData(faceData: FaceDataEntity)

    @Delete
    suspend fun deleteFaceData(faceData: FaceDataEntity)

    @Query("DELETE FROM face_data WHERE person_id = :personId")
    suspend fun deleteFaceDataByPersonId(personId: String)
}
