package com.dabai.app.di
import android.content.Context
import androidx.room.Room
import com.dabai.app.data.local.DabaiDatabase
import com.dabai.app.data.local.dao.*
import com.dabai.app.data.repository.DabaiRepository
import com.dabai.app.service.ai.HealthAIService
import com.dabai.app.service.auth.AuthService
import com.dabai.app.service.camera.CameraService
import com.dabai.app.service.face.FaceRecognitionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(): DabaiDatabase = DabaiDatabase.getInstance()

    @Provides fun provideUserDao(db: DabaiDatabase): UserDao = db.userDao()
    @Provides fun providePersonDao(db: DabaiDatabase): PersonDao = db.personDao()
    @Provides fun provideHealthRecordDao(db: DabaiDatabase): HealthRecordDao = db.healthRecordDao()
    @Provides fun provideActionPlanDao(db: DabaiDatabase): ActionPlanDao = db.actionPlanDao()
    @Provides fun provideActionItemDao(db: DabaiDatabase): ActionItemDao = db.actionItemDao()
    @Provides fun provideFaceDataDao(db: DabaiDatabase): FaceDataDao = db.faceDataDao()

    @Provides
    @Singleton
    fun provideRepository(
        userDao: UserDao, personDao: PersonDao,
        healthRecordDao: HealthRecordDao, actionPlanDao: ActionPlanDao,
        actionItemDao: ActionItemDao, faceDataDao: FaceDataDao
    ): DabaiRepository = DabaiRepository(userDao, personDao, healthRecordDao, actionPlanDao, actionItemDao, faceDataDao)

    @Provides
    @Singleton
    fun provideAuthService(@ApplicationContext context: Context): AuthService = AuthService(context)

    @Provides
    @Singleton
    fun provideHealthAIService(@ApplicationContext context: Context): HealthAIService = HealthAIService(context)

    @Provides
    @Singleton
    fun provideFaceRecognitionService(@ApplicationContext context: Context, faceDataDao: FaceDataDao, personDao: PersonDao): FaceRecognitionService = FaceRecognitionService(context, faceDataDao, personDao)
}
