package com.dabai.app.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dabai.app.data.local.converter.Converters
import com.dabai.app.data.local.dao.*
import com.dabai.app.data.local.entity.*

@Database(
    version = 1,
    entities = [
        UserEntity::class,
        PersonEntity::class,
        HealthRecordEntity::class,
        ActionPlanEntity::class,
        ActionItemEntity::class,
        FaceDataEntity::class
    ],
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class DabaiDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun personDao(): PersonDao
    abstract fun healthRecordDao(): HealthRecordDao
    abstract fun actionPlanDao(): ActionPlanDao
    abstract fun actionItemDao(): ActionItemDao
    abstract fun faceDataDao(): FaceDataDao

    companion object {
        private const val DB_NAME = "dabai_database.db"

        @Volatile
        private var INSTANCE: DabaiDatabase? = null

        fun getInstance(): DabaiDatabase {
            return INSTANCE ?: synchronized(this) {
                val app = com.dabai.app.DabaiApp.instance
                INSTANCE ?: Room.databaseBuilder(
                    app,
                    DabaiDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
