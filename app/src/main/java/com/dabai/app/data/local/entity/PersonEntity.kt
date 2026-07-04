package com.dabai.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "persons",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id"]),
        Index(value = ["face_token"], unique = true)
    ]
)
data class PersonEntity(
    @PrimaryKey
    @ColumnInfo(name = "person_id")
    val personId: String, // UUID

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "face_token")
    val faceToken: String, // 人脸特征哈希值，用于去重

    @ColumnInfo(name = "relationship")
    val relationship: String = "", // 爸爸/妈妈/爷爷/奶奶/哥哥/姐姐/妹妹/弟弟

    @ColumnInfo(name = "nickname")
    val nickname: String = "", // 自动生成2-5字昵称

    @ColumnInfo(name = "avatar_path")
    val avatarPath: String = "", // 本地头像路径

    @ColumnInfo(name = "health_score")
    val healthScore: Float = 0f, // 最近健康评分 0-100

    @ColumnInfo(name = "record_count")
    val recordCount: Int = 0,

    @ColumnInfo(name = "last_photo_path")
    val lastPhotoPath: String = "",

    @ColumnInfo(name = "last_analyzed_at")
    val lastAnalyzedAt: Long = 0L,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
