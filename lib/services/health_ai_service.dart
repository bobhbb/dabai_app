import "dart:math";
import "../models/health_record_model.dart";
import "../config/constants.dart";

class HealthAIService {
  static final HealthAIService _instance = HealthAIService._();
  factory HealthAIService() => _instance;
  HealthAIService._();

  HealthAnalysisResult analyze(String nickname, String relationship, {HealthRecordModel? previous}) {
    final rng = Random();
    final score = 60 + rng.nextInt(36).toDouble();

    final concerns = [];
    if (score < 70) concerns.addAll(["建议保持规律作息", "建议适当增加运动量", "建议注意饮食均衡"]);
    else if (score < 85) concerns.addAll(["建议增加每日饮水量", "建议减少用眼疲劳"]);
    else concerns.add("继续保持良好的生活习惯");

    final strengths = ["家人关系和睦", "积极向上的生活态度"];
    if (score >= 75) strengths.add("整体健康状况良好");

    return HealthAnalysisResult(
      healthScore: score,
      summary: _generateSummary(score, nickname, relationship),
      details: HealthDetails(
        complexion: ["面色红润", "肤色均匀", "略有暗沉", "气色良好"][rng.nextInt(4)],
        eyes: ["眼神明亮", "略有疲劳感", "精神饱满", "眼睛有神"][rng.nextInt(4)],
        spirit: ["精神状态良好", "略显疲惫", "精力充沛", "状态稳定"][rng.nextInt(4)],
        skin: ["皮肤弹性较好", "皮肤略有干燥", "肤质健康"][rng.nextInt(3)],
        bodyShape: ["体型匀称", "略微偏瘦", "体态良好"][rng.nextInt(3)],
        overallAssessment: _generateSummary(score, nickname, relationship),
        concerns: concerns,
        strengths: strengths,
      ),
      actionPlans: _generatePlans(score, nickname, concerns),
    );
  }

  HealthComparison? compare(HealthAnalysisResult current, HealthRecordModel? previous) {
    if (previous == null) return null;
    final diff = current.healthScore - previous.healthScore;
    final status = diff > 3 ? "improved" : diff < -3 ? "declined" : "stable";
    return HealthComparison(
      scoreDiff: diff,
      status: status,
      improvements: diff > 0 ? ["健康评分提升了 ${diff.toStringAsFixed(1)} 分", "整体状态有所改善"] : [],
      regressions: diff < 0 ? ["健康评分下降了 ${diff.abs().toStringAsFixed(1)} 分", "建议关注健康变化"] : [],
      summary: status == "improved" ? "相比上次检查，健康状况有所改善，继续保持！" :
               status == "declined" ? "相比上次检查，需要更加关注健康管理。" :
               "与上次检查相比，健康状况保持稳定。",
    );
  }

  PersonSuggestion suggestPersonInfo() {
    final rng = Random();
    final rel = AppStrings.relationships[rng.nextInt(AppStrings.relationships.length)];
    return PersonSuggestion(relationship: rel, nickname: AppStrings.getNickname(rel));
  }

  String _generateSummary(double score, String name, String rel) {
    if (score >= 85) return "${name}的健康状况非常好！各项指标都很优秀。";
    if (score >= 70) return "${name}的健康状况良好，有小细节可以优化。大白会陪伴${name}一起改善！";
    if (score >= 55) return "${name}的健康状况需要关注，建议从基础习惯开始改善。";
    return "${name}的健康状况需要重视，大白会全力帮助${name}制定改善计划！";
  }

  List<ActionPlanItem> _generatePlans(double score, String name, List<String> concerns) {
    return [
      ActionPlanItem(title: "每日健康习惯", description: "${name}的基础健康习惯建议", frequency: "DAILY", items: [
        ActionItemContent(content: "早起喝一杯温水", dueDaysLater: 0),
        ActionItemContent(content: "保持30分钟户外活动", dueDaysLater: 0),
        ActionItemContent(content: "记录饮食和心情", dueDaysLater: 0),
        ActionItemContent(content: "睡前放松冥想5分钟", dueDaysLater: 0),
      ]),
      ActionPlanItem(title: "每周健康目标", description: "每周固定的健康管理任务", frequency: "WEEKLY", items: [
        ActionItemContent(content: concerns.isNotEmpty ? "【改善】${concerns.first}" : "测量体重并记录", dueDaysLater: 7),
        ActionItemContent(content: "与家人一起做一件开心的事", dueDaysLater: 7),
      ]),
      ActionPlanItem(title: "每月健康回顾", description: "每月系统性评估健康状况", frequency: "MONTHLY", items: [
        ActionItemContent(content: "拍照更新健康报告", dueDaysLater: 30),
        ActionItemContent(content: "回顾本月健康习惯完成情况", dueDaysLater: 30),
        ActionItemContent(content: "调整下月健康计划", dueDaysLater: 30),
      ]),
      if (score < 70)
        ActionPlanItem(title: "年度健康目标", description: "长期健康改善方向", frequency: "YEARLY", items: [
          ActionItemContent(content: "制定年度健康提升计划", dueDaysLater: 365),
          ActionItemContent(content: "培养1-2个新的健康习惯", dueDaysLater: 365),
        ]),
    ];
  }
}

class PersonSuggestion {
  final String relationship;
  final String nickname;
  PersonSuggestion({required this.relationship, required this.nickname});
}
