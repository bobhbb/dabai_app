package com.dabai.app.data.repository

import com.dabai.app.data.local.dao.*
import com.dabai.app.data.local.entity.*
import com.dabai.app.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DabaiRepository @Inject constructor(
    private val userDao: UserDao,
    private val personDao: PersonDao,
    private val healthRecordDao: HealthRecordDao,
    private val actionPlanDao: ActionPlanDao,
    private val actionItemDao: ActionItemDao,
    private val faceDataDao: FaceDataDao
) {

    // ===== User =====
    suspend fun getOrCreateUser(wechatOpenId: String, nickname: String, avatarUrl: String): UserEntity {
        val existing = userDao.getUserByWechatOpenId(wechatOpenId)
        if (existing != null) {
            val updated = existing.copy(nickname = nickname, avatarUrl = avatarUrl)
            userDao.updateUser(updated)
            return updated
        }
        val newUser = UserEntity(
            userId = UUID.randomUUID().toString(),
            wechatOpenId = wechatOpenId,
            nickname = nickname,
            avatarUrl = avatarUrl
        )
        userDao.insertUser(newUser)
        return newUser
    }

    fun observeCurrentUser(): Flow<UserEntity?> = userDao.observeCurrentUser()

    // ===== Person =====
    fun observePersons(userId: String): Flow<List<PersonEntity>> =
        personDao.observePersonsByUserId(userId)

    suspend fun getPersonById(personId: String): PersonEntity? =
        personDao.getPersonById(personId)

    fun observePersonById(personId: String): Flow<PersonEntity?> =
        personDao.observePersonById(personId)

    suspend fun getPersonCount(userId: String): Int =
        personDao.getPersonCountByUserId(userId)

    suspend fun findPersonByFaceToken(faceToken: String): PersonEntity? =
        personDao.getPersonByFaceToken(faceToken)

    suspend fun createNewPerson(
        userId: String,
        faceToken: String,
        relationship: String,
        nickname: String,
        avatarPath: String
    ): PersonEntity {
        val person = PersonEntity(
            personId = UUID.randomUUID().toString(),
            userId = userId,
            faceToken = faceToken,
            relationship = relationship,
            nickname = nickname,
            avatarPath = avatarPath
        )
        personDao.insertPerson(person)
        return person
    }

    suspend fun updatePersonPerson(person: PersonEntity) = personDao.updatePerson(person)

    // ===== Health Record =====
    fun observeHealthRecords(personId: String): Flow<List<HealthRecordEntity>> =
        healthRecordDao.observeRecordsByPersonId(personId)

    suspend fun getLatestRecord(personId: String): HealthRecordEntity? =
        healthRecordDao.getLatestRecord(personId)

    suspend fun getPreviousRecord(personId: String): HealthRecordEntity? =
        healthRecordDao.getSecondLatestRecord(personId)

    suspend fun saveHealthRecord(
        personId: String,
        photoPath: String,
        analysisResult: HealthAnalysisResult,
        comparison: HealthComparison?
    ) {
        val record = HealthRecordEntity(
            recordId = UUID.randomUUID().toString(),
            personId = personId,
            photoPath = photoPath,
            analyzedAt = System.currentTimeMillis(),
            healthScore = analysisResult.healthScore,
            healthSummary = analysisResult.summary,
            healthDetailsJson = kotlinx.serialization.json.Json.encodeToString(
                kotlinx.serialization.json.Json { ignoreUnknownKeys = true },
                analysisResult.details
            ),
            comparisonJson = comparison?.let {
                kotlinx.serialization.json.Json.encodeToString(
                    kotlinx.serialization.json.Json { ignoreUnknownKeys = true },
                    it
                )
            } ?: "",
            improvementStatus = comparison?.status ?: "stable"
        )
        healthRecordDao.insertRecord(record)

        // 保存行动计划
        analysisResult.actionPlans.forEach { planItem ->
            val plan = ActionPlanEntity(
                planId = UUID.randomUUID().toString(),
                personId = personId,
                recordId = record.recordId,
                title = planItem.title,
                description = planItem.description,
                frequency = planItem.frequency,
                sortOrder = analysisResult.actionPlans.indexOf(planItem)
            )
            actionPlanDao.insertPlan(plan)

            val items = planItem.items.mapIndexed { idx, item ->
                ActionItemEntity(
                    itemId = UUID.randomUUID().toString(),
                    planId = plan.planId,
                    content = item.content,
                    dueDate = if (item.dueDaysLater > 0)
                        System.currentTimeMillis() + item.dueDaysLater * 86400000L
                    else 0L,
                    sortOrder = idx
                )
            }
            actionItemDao.insertItems(items)
        }

        // 更新人物概要
        val person = personDao.getPersonById(personId) ?: return
        val updated = person.copy(
            healthScore = analysisResult.healthScore,
            recordCount = person.recordCount + 1,
            lastPhotoPath = photoPath,
            lastAnalyzedAt = record.analyzedAt,
            updatedAt = System.currentTimeMillis()
        )
        personDao.updatePerson(updated)
    }

    // ===== Face Data =====
    suspend fun saveFaceData(
        personId: String, faceFeature: ByteArray,
        imagePath: String, landmarksJson: String = "{}"
    ) {
        val faceData = FaceDataEntity(
            faceId = UUID.randomUUID().toString(),
            personId = personId,
            faceFeature = faceFeature,
            imagePath = imagePath,
            faceLandmarksJson = landmarksJson
        )
        faceDataDao.insertFaceData(faceData)
    }

    // ===== Action Plan =====
    fun observePlans(personId: String): Flow<List<ActionPlanEntity>> =
        actionPlanDao.observePlansByPersonId(personId)

    fun observeActivePlans(personId: String): Flow<List<ActionPlanEntity>> =
        actionPlanDao.observeActivePlansByPersonId(personId)

    fun observePlanItems(planId: String): Flow<List<ActionItemEntity>> =
        actionItemDao.observeItemsByPlanId(planId)

    suspend fun toggleActionItem(itemId: String, isChecked: Boolean) {
        actionItemDao.updateItemCheck(itemId, isChecked, if (isChecked) System.currentTimeMillis() else 0L)
    }

    suspend fun completePlan(planId: String) {
        actionPlanDao.updatePlanCompletion(planId, true)
    }
}
