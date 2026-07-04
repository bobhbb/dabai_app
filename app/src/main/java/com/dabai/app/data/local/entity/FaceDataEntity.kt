package com.dabai.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "face_data",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["person_id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["person_id"])]
)
data class FaceDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "face_id")
    val faceId: String, // UUID

    @ColumnInfo(name = "person_id")
    val personId: String,

    @ColumnInfo(name = "face_feature")
    val faceFeature: ByteArray, // 人脸特征向量 (float array serialized)

    @ColumnInfo(name = "image_path")
    val imagePath: String,

    @ColumnInfo(name = "face_landmarks_json")
    val faceLandmarksJson: String = "{}",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FaceDataEntity) return false
        return faceId == other.faceId
    }

    override fun hashCode(): Int = faceId.hashCode()
}
