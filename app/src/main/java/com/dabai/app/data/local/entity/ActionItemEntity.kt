package com.dabai.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "action_items",
    foreignKeys = [
        ForeignKey(
            entity = ActionPlanEntity::class,
            parentColumns = ["plan_id"],
            childColumns = ["plan_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["plan_id"]), Index(value = ["is_checked"]), Index(value = ["due_date"])]
)
data class ActionItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "item_id")
    val itemId: String, // UUID

    @ColumnInfo(name = "plan_id")
    val planId: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "is_checked")
    val isChecked: Boolean = false,

    @ColumnInfo(name = "checked_at")
    val checkedAt: Long = 0L,

    @ColumnInfo(name = "due_date")
    val dueDate: Long = 0L,

    @ColumnInfo(name = "sort_order")
    val sortOrder: Int = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
