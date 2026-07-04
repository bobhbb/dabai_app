package com.dabai.app.data.local.converter

import androidx.room.TypeConverter
import java.util.UUID

class Converters {
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? = uuid?.toString()

    @TypeConverter
    fun toUUID(value: String?): UUID? = value?.let { UUID.fromString(it) }

    @TypeConverter
    fun fromByteArray(bytes: ByteArray?): String? =
        bytes?.joinToString(",") { it.toString() }

    @TypeConverter
    fun toByteArray(value: String?): ByteArray? =
        value?.split(",")?.mapNotNull { it.toByteOrNull() }?.toByteArray()
}
