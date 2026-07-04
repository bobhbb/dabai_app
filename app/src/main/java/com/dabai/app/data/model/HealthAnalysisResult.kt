package com.dabai.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HealthAnalysisResult(
    val healthScore: Float,
    val summary: String,
    val details: HealthDetails,
    val actionPlans: List<ActionPlanItem>
)

@Serializable
data class HealthDetails(
    val complexion: String = "",
    val eyes: String = "",
    val spirit: String = "",
    val skin: String = "",
    val bodyShape: String = "",
    val overallAssessment: String = "",
    val concerns: List<String> = emptyList(),
    val strengths: List<String> = emptyList()
)

@Serializable
data class ActionPlanItem(
    val title: String,
    val description: String = "",
    val frequency: String, // DAILY / WEEKLY / MONTHLY / YEARLY
    val items: List<ActionItemContent>
)

@Serializable
data class ActionItemContent(
    val content: String,
    val dueDaysLater: Int = 0 // 0 = today
)

@Serializable
data class HealthComparison(
    val scoreDiff: Float,
    val status: String, // improved / declined / stable
    val improvements: List<String> = emptyList(),
    val regressions: List<String> = emptyList(),
    val summary: String = ""
)

@Serializable
data class PersonRelationshipSuggestion(
    val relationship: String,
    val nickname: String,
    val relationshipColor: String = ""
)

@Serializable
data class UserProfile(
    val userId: String,
    val nickname: String,
    val avatarUrl: String = ""
)

@Serializable
data class PersonProfile(
    val personId: String,
    val nickname: String,
    val relationship: String,
    val avatarPath: String,
    val healthScore: Float,
    val recordCount: Int,
    val lastAnalyzedAt: Long,
    val createdAt: Long
)

@Serializable
data class HealthReportSummary(
    val recordId: String,
    val analyzedAt: Long,
    val score: Float,
    val summary: String,
    val improvementStatus: String,
    val comparisonSummary: String = ""
)
