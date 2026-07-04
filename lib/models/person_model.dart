class PersonModel {
  final String personId;
  final String userId;
  final String faceToken;
  final String relationship;
  final String nickname;
  final String avatarPath;
  final double healthScore;
  final int recordCount;
  final String lastPhotoPath;
  final int lastAnalyzedAt;
  final DateTime createdAt;
  final DateTime updatedAt;

  PersonModel({
    required this.personId,
    required this.userId,
    required this.faceToken,
    this.relationship = "",
    this.nickname = "",
    this.avatarPath = "",
    this.healthScore = 0,
    this.recordCount = 0,
    this.lastPhotoPath = "",
    this.lastAnalyzedAt = 0,
    DateTime? createdAt,
    DateTime? updatedAt,
  })  : createdAt = createdAt ?? DateTime.now(),
        updatedAt = updatedAt ?? DateTime.now();

  Map<String, dynamic> toJson() => {
    "personId": personId,
    "userId": userId,
    "faceToken": faceToken,
    "relationship": relationship,
    "nickname": nickname,
    "avatarPath": avatarPath,
    "healthScore": healthScore,
    "recordCount": recordCount,
    "lastPhotoPath": lastPhotoPath,
    "lastAnalyzedAt": lastAnalyzedAt,
    "createdAt": createdAt.toIso8601String(),
    "updatedAt": updatedAt.toIso8601String(),
  };

  factory PersonModel.fromJson(Map<String, dynamic> json) => PersonModel(
    personId: json["personId"] ?? "",
    userId: json["userId"] ?? "",
    faceToken: json["faceToken"] ?? "",
    relationship: json["relationship"] ?? "",
    nickname: json["nickname"] ?? "",
    avatarPath: json["avatarPath"] ?? "",
    healthScore: (json["healthScore"] ?? 0).toDouble(),
    recordCount: json["recordCount"] ?? 0,
    lastPhotoPath: json["lastPhotoPath"] ?? "",
    lastAnalyzedAt: json["lastAnalyzedAt"] ?? 0,
    createdAt: DateTime.tryParse(json["createdAt"] ?? "") ?? DateTime.now(),
    updatedAt: DateTime.tryParse(json["updatedAt"] ?? "") ?? DateTime.now(),
  );

  PersonModel copyWith({
    String? relationship,
    String? nickname,
    String? avatarPath,
    double? healthScore,
    int? recordCount,
    String? lastPhotoPath,
    int? lastAnalyzedAt,
  }) => PersonModel(
    personId: personId,
    userId: userId,
    faceToken: faceToken,
    relationship: relationship ?? this.relationship,
    nickname: nickname ?? this.nickname,
    avatarPath: avatarPath ?? this.avatarPath,
    healthScore: healthScore ?? this.healthScore,
    recordCount: recordCount ?? this.recordCount,
    lastPhotoPath: lastPhotoPath ?? this.lastPhotoPath,
    lastAnalyzedAt: lastAnalyzedAt ?? this.lastAnalyzedAt,
    createdAt: createdAt,
    updatedAt: DateTime.now(),
  );
}
