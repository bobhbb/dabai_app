package com.dabai.app.data.local.dao

import androidx.room.*
import com.dabai.app.data.local.entity.ActionPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionPlanDao {
    @Query("SELECT * FROM action_plans WHERE plan_id = :planId")
    suspend fun getPlanById(planId: String): ActionPlanEntity?

    @Query("SELECT * FROM action_plans WHERE person_id = :personId ORDER BY sort_order ASC")
    fun observePlansByPersonId(personId: String): Flow<List<ActionPlanEntity>>

    @Query("SELECT * FROM action_plans WHERE person_id = :personId AND is_completed = 0 ORDER BY sort_order ASC")
    fun observeActivePlansByPersonId(personId: String): Flow<List<ActionPlanEntity>>

    @Query("SELECT * FROM action_plans WHERE record_id = :recordId ORDER BY sort_order ASC")
    suspend fun getPlansByRecordId(recordId: String): List<ActionPlanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: ActionPlanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlans(plans: List<ActionPlanEntity>)

    @Update
    suspend fun updatePlan(plan: ActionPlanEntity)

    @Delete
    suspend fun deletePlan(plan: ActionPlanEntity)

    @Query("UPDATE action_plans SET is_completed = :isCompleted WHERE plan_id = :planId")
    suspend fun updatePlanCompletion(planId: String, isCompleted: Boolean)
}
