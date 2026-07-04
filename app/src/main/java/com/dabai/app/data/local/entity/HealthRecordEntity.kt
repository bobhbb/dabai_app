package com.dabai.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "health_records",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["person_id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["person_id"]), Index(value = ["analyzed_at"])]
)
data class HealthRecordEntity(
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    val recordId: String, // UUID

    @ColumnInfo(name = "person_id")
    val personId: String,

    @ColumnInfo(name = "photo_path")
    val photoPath: String,

    @ColumnInfo(name = "analyzed_at")
    val analyzedAt: Long,

    @ColumnInfo(name = "health_score")
    val healthScore: Float, // 0-100

    @ColumnInfo(name = "health_summary")
    val healthSummary: String, // AI分析摘要

    @ColumnInfo(name = "health_details_json")
    val healthDetailsJson: String, // AI详细分析JSON

    @ColumnInfo(name = "comparison_json")
    val comparisonJson: String = "", // 与上次的对比分析JSON

    @ColumnInfo(name = "improvement_status")
    val improvementStatus: String = "stable", // improved/declined/stable

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
