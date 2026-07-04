import "package:flutter/material.dart";
import "package:provider/provider.dart";
import "../providers/app_state.dart";
import "../models/health_record_model.dart";
import "../widgets/health_score_bar.dart";
import "../config/constants.dart";

class HealthReportScreen extends StatelessWidget {
  final String personId;
  final String recordId;
  const HealthReportScreen({super.key, required this.personId, required this.recordId});

  @override
  Widget build(BuildContext context) {
    final state = context.watch<AppState>();
    final record = state.records.firstWhere((r) => r.recordId == recordId);
    final details = record.healthDetails;

    return Scaffold(
      backgroundColor: const Color(DabaiColors.background),
      appBar: AppBar(title: const Text("健康报告", style: TextStyle(fontWeight: FontWeight.bold))),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Score Card
            _ScoreCard(score: record.healthScore, comparison: record.comparisonJson),
            const SizedBox(height: 16),

            // Comparison
            if (record.improvementStatus != "stable" || record.comparisonJson.isNotEmpty)
              _ComparisonCard(status: record.improvementStatus, summary: record.comparisonJson),
            if (record.improvementStatus != "stable" || record.comparisonJson.isNotEmpty)
              const SizedBox(height: 16),

            // Details
            const Text("详细分析", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            if (details["complexion"] != null) _DetailItem("面色", details["complexion"]),
            if (details["eyes"] != null) _DetailItem("眼神", details["eyes"]),
            if (details["spirit"] != null) _DetailItem("精神", details["spirit"]),
            if (details["skin"] != null) _DetailItem("皮肤", details["skin"]),
            if (details["bodyShape"] != null) _DetailItem("体型", details["bodyShape"]),
            if (details["overallAssessment"] != null) _DetailItem("总体评估", details["overallAssessment"]),

            const SizedBox(height: 16),
            Text("健康建议", style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            if (details["concerns"] is List)
              ...(details["concerns"] as List).map((c) => _AdviceItem("⚠️", c.toString())),
            if (details["strengths"] is List)
              ...(details["strengths"] as List).map((s) => _AdviceItem("✅", s.toString())),
          ],
        ),
      ),
    );
  }
}

class _ScoreCard extends StatelessWidget {
  final double score;
  final String comparison;
  const _ScoreCard({required this.score, required this.comparison});

  @override
  Widget build(BuildContext context) {
    final color = score >= 75 ? const Color(DabaiColors.healthGood) : score >= 55 ? const Color(DabaiColors.healthWarn) : const Color(DabaiColors.healthBad);
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          children: [
            const Text("综合健康评分", style: TextStyle(fontSize: 14, color: Color(DabaiColors.subtext))),
            const SizedBox(height: 8),
            Text("${score.toInt()}", style: TextStyle(fontSize: 64, fontWeight: FontWeight.bold, color: color)),
            const Text("分", style: TextStyle(fontSize: 18, color: Color(DabaiColors.subtext))),
            const SizedBox(height: 12),
            HealthScoreBar(score: score, color: color, showText: false),
          ],
        ),
      ),
    );
  }
}

class _ComparisonCard extends StatelessWidget {
  final String status;
  final String summary;
  const _ComparisonCard({required this.status, required this.summary});

  @override
  Widget build(BuildContext context) {
    final (icon, statusText, statusColor) = status == "improved"
        ? ("📈", "有改善", const Color(DabaiColors.healthGood))
        : status == "declined"
            ? ("📉", "需关注", const Color(DabaiColors.healthBad))
            : ("➡️", "保持稳定", const Color(DabaiColors.subtext));
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Row(
          children: [
            Text(icon, style: const TextStyle(fontSize: 20)),
            const SizedBox(width: 8),
            const Text("对比上次报告", style: TextStyle(fontWeight: FontWeight.bold)),
            const Spacer(),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
              decoration: BoxDecoration(color: statusColor.withOpacity(0.15), borderRadius: BorderRadius.circular(12)),
              child: Text(statusText, style: TextStyle(color: statusColor, fontSize: 12, fontWeight: FontWeight.w500)),
            ),
          ],
        ),
      ),
    );
  }
}

class _DetailItem extends StatelessWidget {
  final String label, value;
  const _DetailItem(this.label, this.value);
  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.only(bottom: 4),
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        child: Row(
          children: [
            SizedBox(width: 72, child: Text(label, style: const TextStyle(fontSize: 14, fontWeight: FontWeight.w500, color: Color(DabaiColors.subtext)))),
            const SizedBox(width: 12),
            Expanded(child: Text(value, style: const TextStyle(fontSize: 14))),
          ],
        ),
      ),
    );
  }
}

class _AdviceItem extends StatelessWidget {
  final String icon, text;
  const _AdviceItem(this.icon, this.text);
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 4),
      child: Row(
        children: [
          Text(icon),
          const SizedBox(width: 8),
          Expanded(child: Text(text, style: const TextStyle(fontSize: 14))),
        ],
      ),
    );
  }
}
