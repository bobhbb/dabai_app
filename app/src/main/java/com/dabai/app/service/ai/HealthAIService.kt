package com.dabai.app.service.ai

import android.content.Context
import com.dabai.app.data.model.*
import com.dabai.app.data.local.entity.HealthRecordEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AI 健康分析服务
 *
 * 核心能力：
 * 1. 分析人物照片，评估健康状态
 * 2. 生成个性化健康报告
 * 3. 对比上次报告，生成改善对比
 * 4. 生成行动计划（每日/每周/每月/每年）
 * 5. 自动推荐家人关系称呼和昵称
 *
 * 生产环境中接入大模型API（如 GPT-4V / Claude Vision），
 * 当前使用规则引擎+随机生成作为演示占位。
 */
@Singleton
class HealthAIService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * 分析人物照片并生成健康报告 + 行动计划
     */
    suspend fun analyzeHealth(
        photoPath: String,
        personNickname: String,
        personRelationship: String,
        previousRecord: HealthRecordEntity? = null
    ): HealthAnalysisResult = withContext(Dispatchers.Default) {
        // -----------------------------------------------------------------
        // 生产环境：调用多模态大模型API分析照片
        // val prompt = buildAnalysisPrompt(photoPath, personNickname, personRelationship, previousRecord)
        // val response = callLLMApi(prompt)
        // return parseLLMResponse(response)
        // -----------------------------------------------------------------

        // 演示：规则引擎生成示例数据
        val healthScore = (60..95).random().toFloat()
        val concerns = generateConcerns(healthScore)
        val strengths = generateStrengths(personRelationship)

        HealthAnalysisResult(
            healthScore = healthScore,
            summary = generateSummary(healthScore, personNickname, personRelationship),
            details = HealthDetails(
                complexion = listOf("面色红润", "肤色均匀", "略有暗沉", "气色良好").random(),
                eyes = listOf("眼神明亮", "略有疲劳感", "精神饱满", "眼睛有神").random(),
                spirit = listOf("精神状态良好", "略显疲惫", "精力充沛", "状态稳定").random(),
                skin = listOf("皮肤弹性较好", "皮肤略有干燥", "肤质健康", "需要注意保湿").random(),
                bodyShape = listOf("体型匀称", "略微偏瘦", "略微偏胖", "体态良好").random(),
                overallAssessment = generateSummary(healthScore, personNickname, personRelationship),
                concerns = concerns,
                strengths = strengths
            ),
            actionPlans = generateActionPlans(healthScore, personNickname, concerns)
        )
    }

    /**
     * 对比两次健康报告
     */
    suspend fun compareHealth(
        current: HealthAnalysisResult,
        previous: HealthRecordEntity?,
        previousResult: HealthAnalysisResult?
    ): HealthComparison? {
        if (previous == null || previousResult == null) return null

        val scoreDiff = current.healthScore - previous.healthScore
        val status = when {
            scoreDiff > 5 -> "improved"
            scoreDiff < -5 -> "declined"
            else -> "stable"
        }

        return HealthComparison(
            scoreDiff = scoreDiff,
            status = status,
            improvements = if (scoreDiff > 0)
                listOf("健康评分提升了 ${"%.1f".format(scoreDiff)} 分", "整体状态有所改善")
            else emptyList(),
            regressions = if (scoreDiff < 0)
                listOf("健康评分下降了 ${"%.1f".format(Math.abs(scoreDiff))} 分", "建议关注健康变化")
            else emptyList(),
            summary = when (status) {
                "improved" -> "相比上次检查，${previous?.let { "健康状态有所改善" } ?: ""}继续保持！"
                "declined" -> "相比上次检查，需要更加关注健康管理。"
                else -> "与上次检查相比，健康状况保持稳定。"
            }
        )
    }

    /**
     * 根据人物外貌特征自动推荐关系和昵称
     */
    suspend fun suggestRelationshipAndNickname(
        photoPath: String,
        userNickname: String = ""
    ): PersonRelationshipSuggestion = withContext(Dispatchers.Default) {
        // -----------------------------------------------------------------
        // 生产环境：通过大模型分析照片中人物特征推断年龄/性别/关系
        // -----------------------------------------------------------------
        val relationships = listOf("爸爸", "妈妈", "爷爷", "奶奶", "哥哥", "姐姐", "妹妹", "弟弟")
        val relationship = relationships.random()

        val nickname = generateNickname(relationship)

        PersonRelationshipSuggestion(
            relationship = relationship,
            nickname = nickname
        )
    }

    private fun generateSummary(score: Float, name: String, relationship: String): String {
        return when {
            score >= 85 -> "${name}的健康状况非常好！各项指标都很优秀，请继续保持健康的生活方式。"
            score >= 70 -> "${name}的健康状况良好，有一些小细节可以进一步优化。大白会陪伴${name}一起改善！"
            score >= 55 -> "${name}的健康状况需要关注，大白建议从基础习惯开始改善。"
            else -> "${name}的健康状况需要重视，大白会全力帮助${name}制定改善计划！"
        }
    }

    private fun generateConcerns(score: Float): List<String> {
        val all = mutableListOf(
            "建议增加每日饮水量",
            "建议保持规律作息",
            "建议适当增加运动量",
            "建议减少电子产品使用时间",
            "建议注意饮食均衡",
            "建议保持心情愉悦"
        )
        val count = when {
            score >= 85 -> 1
            score >= 70 -> 2
            score >= 55 -> 3
            else -> 4
        }
        return all.shuffled().take(count)
    }

    private fun generateStrengths(relationship: String): List<String> {
        return listOf(
            "家人关系和睦",
            "有良好的家庭支持",
            "积极向上的生活态度"
        )
    }

    private fun generateActionPlans(score: Float, name: String, concerns: List<String>): List<ActionPlanItem> {
        val plans = mutableListOf<ActionPlanItem>()

        // 每日计划
        plans.add(ActionPlanItem(
            title = "☀️ 每日健康习惯",
            description = "${name}的基础健康习惯建议",
            frequency = "DAILY",
            items = listOf(
                ActionItemContent("早起喝一杯温水", 0),
                ActionItemContent("保持30分钟户外活动", 0),
                ActionItemContent("记录饮食和心情", 0),
                ActionItemContent("睡前放松冥想5分钟", 0)
            )
        ))

        // 每周计划
        plans.add(ActionPlanItem(
            title = "📅 每周健康目标",
            description = "每周固定的健康管理任务",
            frequency = "WEEKLY",
            items = concerns.take(2).mapIndexed { i, concern ->
                ActionItemContent("【改善】$concern", 7)
            } + listOf(
                ActionItemContent("测量体重并记录", 7),
                ActionItemContent("与家人一起做一件开心的事", 7)
            )
        ))

        // 每月计划
        plans.add(ActionPlanItem(
            title = "📊 每月健康回顾",
            description = "每月系统性地评估健康状况",
            frequency = "MONTHLY",
            items = listOf(
                ActionItemContent("拍照更新健康报告", 30),
                ActionItemContent("回顾本月健康习惯完成情况", 30),
                ActionItemContent("调整下月健康计划", 30)
            )
        ))

        if (score < 70) {
            plans.add(ActionPlanItem(
                title = "🎯 年度健康目标",
                description = "长期健康改善方向",
                frequency = "YEARLY",
                items = listOf(
                    ActionItemContent("制定年度健康提升计划", 365),
                    ActionItemContent("定期进行全面健康检查", 365),
                    ActionItemContent("培养1-2个新的健康习惯", 365)
                )
            ))
        }

        return plans
    }

    /**
     * 自动生成昵称（2-5字）
     * 规则：根据关系类型和文化习惯生成亲切的昵称
     */
    private fun generateNickname(relationship: String): String {
        val nicknames = mapOf(
            "爸爸" to listOf("老爸", "老爹", "阿爸", "爸比", "老豆"),
            "妈妈" to listOf("老妈", "娘亲", "阿妈", "妈咪", "老母"),
            "爷爷" to listOf("爷爷", "阿公", "老爷爷", "爷爷大人"),
            "奶奶" to listOf("奶奶", "阿婆", "老奶奶", "奶奶大人"),
            "哥哥" to listOf("老哥", "大哥", "阿哥", "哥仔", "兄长"),
            "姐姐" to listOf("老姐", "大姐", "阿姐", "姐姐大人", "姐仔"),
            "妹妹" to listOf("小妹", "老妹", "阿妹", "妹妹仔"),
            "弟弟" to listOf("老弟", "小弟", "阿弟", "弟弟仔")
        )
        return nicknames[relationship]?.random() ?: "家人"
    }

}
