package com.dabai.app.data.local.dao

import androidx.room.*
import com.dabai.app.data.local.entity.ActionItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionItemDao {
    @Query("SELECT * FROM action_items WHERE item_id = :itemId")
    suspend fun getItemById(itemId: String): ActionItemEntity?

    @Query("SELECT * FROM action_items WHERE plan_id = :planId ORDER BY sort_order ASC")
    fun observeItemsByPlanId(planId: String): Flow<List<ActionItemEntity>>

    @Query("SELECT * FROM action_items WHERE plan_id = :planId ORDER BY sort_order ASC")
    suspend fun getItemsByPlanId(planId: String): List<ActionItemEntity>

    @Query("SELECT * FROM action_items WHERE plan_id = :planId AND is_checked = 0 ORDER BY sort_order ASC")
    fun observeUncheckedItemsByPlanId(planId: String): Flow<List<ActionItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ActionItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ActionItemEntity>)

    @Update
    suspend fun updateItem(item: ActionItemEntity)

    @Query("UPDATE action_items SET is_checked = :isChecked, checked_at = :checkedAt WHERE item_id = :itemId")
    suspend fun updateItemCheck(itemId: String, isChecked: Boolean, checkedAt: Long)

    @Delete
    suspend fun deleteItem(item: ActionItemEntity)
}
