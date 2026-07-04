class ActionPlanModel {
  final String planId;
  final String personId;
  final String recordId;
  final String title;
  final String description;
  final String frequency;
  final int startDate;
  final int endDate;
  final bool isCompleted;
  final int sortOrder;
  final List<ActionItemModel> items;

  ActionPlanModel({
    required this.planId,
    required this.personId,
    required this.recordId,
    required this.title,
    this.description = "",
    required this.frequency,
    this.startDate = 0,
    this.endDate = 0,
    this.isCompleted = false,
    this.sortOrder = 0,
    this.items = const [],
  });

  Map<String, dynamic> toJson() => {
    "planId": planId,
    "personId": personId,
    "recordId": recordId,
    "title": title,
    "description": description,
    "frequency": frequency,
    "startDate": startDate,
    "endDate": endDate,
    "isCompleted": isCompleted,
    "sortOrder": sortOrder,
  };

  factory ActionPlanModel.fromJson(Map<String, dynamic> json) => ActionPlanModel(
    planId: json["planId"] ?? "",
    personId: json["personId"] ?? "",
    recordId: json["recordId"] ?? "",
    title: json["title"] ?? "",
    description: json["description"] ?? "",
    frequency: json["frequency"] ?? "DAILY",
    startDate: json["startDate"] ?? 0,
    endDate: json["endDate"] ?? 0,
    isCompleted: json["isCompleted"] ?? false,
    sortOrder: json["sortOrder"] ?? 0,
  );

  ActionPlanModel copyWith({bool? isCompleted, List<ActionItemModel>? items}) =>
      ActionPlanModel(
        planId: planId,
        personId: personId,
        recordId: recordId,
        title: title,
        description: description,
        frequency: frequency,
        startDate: startDate,
        endDate: endDate,
        isCompleted: isCompleted ?? this.isCompleted,
        sortOrder: sortOrder,
        items: items ?? this.items,
      );
}

class ActionItemModel {
  final String itemId;
  final String planId;
  final String content;
  final bool isChecked;
  final int checkedAt;
  final int dueDate;
  final int sortOrder;

  ActionItemModel({
    required this.itemId,
    required this.planId,
    required this.content,
    this.isChecked = false,
    this.checkedAt = 0,
    this.dueDate = 0,
    this.sortOrder = 0,
  });

  Map<String, dynamic> toJson() => {
    "itemId": itemId,
    "planId": planId,
    "content": content,
    "isChecked": isChecked,
    "checkedAt": checkedAt,
    "dueDate": dueDate,
    "sortOrder": sortOrder,
  };

  factory ActionItemModel.fromJson(Map<String, dynamic> json) => ActionItemModel(
    itemId: json["itemId"] ?? "",
    planId: json["planId"] ?? "",
    content: json["content"] ?? "",
    isChecked: json["isChecked"] ?? false,
    checkedAt: json["checkedAt"] ?? 0,
    dueDate: json["dueDate"] ?? 0,
    sortOrder: json["sortOrder"] ?? 0,
  );

  ActionItemModel copyWith({bool? isChecked, int? checkedAt}) =>
      ActionItemModel(
        itemId: itemId,
        planId: planId,
        content: content,
        isChecked: isChecked ?? this.isChecked,
        checkedAt: checkedAt ?? this.checkedAt,
        dueDate: dueDate,
        sortOrder: sortOrder,
      );
}
