import 'dart:convert';

class HealthRecordModel {
  final String recordId;
  final String personId;
  final String photoPath;
  final int analyzedAt;
  final double healthScore;
  final String healthSummary;
  final Map<String, dynamic> healthDetails;
  final String comparisonJson;
  final String improvementStatus;

  HealthRecordModel({
    required this.recordId,
    required this.personId,
    required this.photoPath,
    required this.analyzedAt,
    required this.healthScore,
    required this.healthSummary,
    this.healthDetails = const {},
    this.comparisonJson = "",
    this.improvementStatus = "stable",
  });

  Map<String, dynamic> toJson() => {
    "recordId": recordId,
    "personId": personId,
    "photoPath": photoPath,
    "analyzedAt": analyzedAt,
    "healthScore": healthScore,
    "healthSummary": healthSummary,
    "healthDetails": healthDetails,
    "comparisonJson": comparisonJson,
    "improvementStatus": improvementStatus,
  };

  factory HealthRecordModel.fromJson(Map<String, dynamic> json) => HealthRecordModel(
    recordId: json["recordId"] ?? "",
    personId: json["personId"] ?? "",
    photoPath: json["photoPath"] ?? "",
    analyzedAt: json["analyzedAt"] ?? 0,
    healthScore: (json["healthScore"] ?? 0).toDouble(),
    healthSummary: json["healthSummary"] ?? "",
    healthDetails: json["healthDetails"] is Map ? Map<String, dynamic>.from(json["healthDetails"]) : {},
    comparisonJson: json["comparisonJson"] ?? "",
    improvementStatus: json["improvementStatus"] ?? "stable",
  );
}

class HealthAnalysisResult {
  final double healthScore;
  final String summary;
  final HealthDetails details;
  final List<ActionPlanItem> actionPlans;

  HealthAnalysisResult({
    required this.healthScore,
    required this.summary,
    required this.details,
    required this.actionPlans,
  });
}

class HealthDetails {
  final String complexion;
  final String eyes;
  final String spirit;
  final String skin;
  final String bodyShape;
  final String overallAssessment;
  final List<String> concerns;
  final List<String> strengths;

  HealthDetails({
    this.complexion = "",
    this.eyes = "",
    this.spirit = "",
    this.skin = "",
    this.bodyShape = "",
    this.overallAssessment = "",
    this.concerns = const [],
    this.strengths = const [],
  });
}

class ActionPlanItem {
  final String title;
  final String description;
  final String frequency;
  final List<ActionItemContent> items;

  ActionPlanItem({
    required this.title,
    this.description = "",
    required this.frequency,
    required this.items,
  });
}

class ActionItemContent {
  final String content;
  final int dueDaysLater;

  ActionItemContent({required this.content, this.dueDaysLater = 0});
}

class HealthComparison {
  final double scoreDiff;
  final String status;
  final List<String> improvements;
  final List<String> regressions;
  final String summary;

  HealthComparison({
    required this.scoreDiff,
    required this.status,
    this.improvements = const [],
    this.regressions = const [],
    this.summary = "",
  });
}
