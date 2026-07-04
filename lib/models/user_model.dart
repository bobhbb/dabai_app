class UserModel {
  final String userId;
  final String nickname;
  final String avatarUrl;
  final DateTime createdAt;

  UserModel({
    required this.userId,
    this.nickname = "",
    this.avatarUrl = "",
    DateTime? createdAt,
  }) : createdAt = createdAt ?? DateTime.now();

  Map<String, dynamic> toJson() => {
    "userId": userId,
    "nickname": nickname,
    "avatarUrl": avatarUrl,
    "createdAt": createdAt.toIso8601String(),
  };

  factory UserModel.fromJson(Map<String, dynamic> json) => UserModel(
    userId: json["userId"] ?? "",
    nickname: json["nickname"] ?? "",
    avatarUrl: json["avatarUrl"] ?? "",
    createdAt: DateTime.tryParse(json["createdAt"] ?? "") ?? DateTime.now(),
  );
}
