package com.dabai.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "action_plans",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["person_id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = HealthRecordEntity::class,
            parentColumns = ["record_id"],
            childColumns = ["record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["person_id"]), Index(value = ["record_id"]), Index(value = ["frequency"])]
)
data class ActionPlanEntity(
    @PrimaryKey
    @ColumnInfo(name = "plan_id")
    val planId: String, // UUID

    @ColumnInfo(name = "person_id")
    val personId: String,

    @ColumnInfo(name = "record_id")
    val recordId: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "frequency")
    val frequency: String, // DAILY / WEEKLY / MONTHLY / YEARLY

    @ColumnInfo(name = "start_date")
    val startDate: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "end_date")
    val endDate: Long = 0L,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "sort_order")
    val sortOrder: Int = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
