package com.dabai.app.data.local.dao

import androidx.room.*
import com.dabai.app.data.local.entity.PersonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Query("SELECT * FROM persons WHERE person_id = :personId")
    suspend fun getPersonById(personId: String): PersonEntity?

    @Query("SELECT * FROM persons WHERE person_id = :personId")
    fun observePersonById(personId: String): Flow<PersonEntity?>

    @Query("SELECT * FROM persons WHERE user_id = :userId ORDER BY created_at ASC")
    fun observePersonsByUserId(userId: String): Flow<List<PersonEntity>>

    @Query("SELECT * FROM persons WHERE user_id = :userId ORDER BY created_at ASC")
    suspend fun getPersonsByUserId(userId: String): List<PersonEntity>

    @Query("SELECT COUNT(*) FROM persons WHERE user_id = :userId")
    suspend fun getPersonCountByUserId(userId: String): Int

    @Query("SELECT * FROM persons WHERE face_token = :faceToken LIMIT 1")
    suspend fun getPersonByFaceToken(faceToken: String): PersonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: PersonEntity)

    @Update
    suspend fun updatePerson(person: PersonEntity)

    @Delete
    suspend fun deletePerson(person: PersonEntity)
}
