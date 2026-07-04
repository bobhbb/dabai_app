import "package:flutter/material.dart";
import "../models/user_model.dart";
import "../models/person_model.dart";
import "../models/health_record_model.dart";
import "../models/action_plan_model.dart";
import "../services/auth_service.dart";
import "../services/database_service.dart";
import "../services/camera_service.dart";
import "../services/health_ai_service.dart";
import "package:uuid/uuid.dart";

class AppState extends ChangeNotifier {
  final AuthService _auth = AuthService();
  final DatabaseService _db = DatabaseService();
  final HealthAIService _ai = HealthAIService();
  final CameraService _cam = CameraService();
  final FaceService _face = FaceService();

  bool _initialized = false;
  bool get initialized => _initialized;

  UserModel? get currentUser => _auth.currentUser;
  bool get isLoggedIn => _auth.isLoggedIn;

  List<PersonModel> _persons = [];
  List<PersonModel> get persons => _persons;
  int get personCount => _persons.length;
  bool get canAddPerson => _persons.length < 3;

  PersonModel? _selectedPerson;
  PersonModel? get selectedPerson => _selectedPerson;

  List<HealthRecordModel> _records = [];
  List<HealthRecordModel> get records => _records;

  List<ActionPlanModel> _plans = [];
  List<ActionPlanModel> get plans => _plans;

  bool _isAnalyzing = false;
  bool get isAnalyzing => _isAnalyzing;

  String? _error;
  String? get error => _error;

  // Init
  Future<void> initialize() async {
    await _auth.init();
    if (_auth.isLoggedIn) {
      await loadPersons();
    }
    _initialized = true;
    notifyListeners();
  }

  // Login
  Future<void> login() async {
    await _auth.loginWithWeChat();
    await loadPersons();
    notifyListeners();
  }

  Future<void> logout() async {
    await _auth.logout();
    _persons = [];
    _records = [];
    _plans = [];
    _selectedPerson = null;
    notifyListeners();
  }

  // Persons
  Future<void> loadPersons() async {
    _persons = await _db.getPersons();
    notifyListeners();
  }

  void selectPerson(PersonModel person) {
    _selectedPerson = person;
    notifyListeners();
  }

  // Camera & Analysis
  Future<CaptureFlowResult?> captureAndAnalyze() async {
    _error = null;
    final photo = await _cam.takePhoto();
    if (photo == null) return null;

    _isAnalyzing = true;
    notifyListeners();

    try {
      final faceResult = await _face.analyze(photo.path);

      PersonModel person;
      bool isNew;

      if (faceResult.isKnown && faceResult.matchedPerson != null) {
        person = faceResult.matchedPerson!;
        isNew = false;
      } else {
        if (_persons.length >= 3) {
          _error = "已达3人上限";
          _isAnalyzing = false;
          notifyListeners();
          return null;
        }
        final suggestion = _ai.suggestPersonInfo();
        person = PersonModel(
          personId: DatabaseService.generateUUID(),
          userId: currentUser?.userId ?? "",
          faceToken: faceResult.faceToken,
          relationship: suggestion.relationship,
          nickname: suggestion.nickname,
          lastPhotoPath: photo.path,
        );
        await _db.savePerson(person);
        _persons.add(person);
        isNew = true;
      }

      final previous = await _db.getLatestRecord(person.personId);
      final result = _ai.analyze(person.nickname, person.relationship, previous: previous);
      final comparison = _ai.compare(result, previous);

      // Save health record
      final record = HealthRecordModel(
        recordId: DatabaseService.generateUUID(),
        personId: person.personId,
        photoPath: photo.path,
        analyzedAt: DateTime.now().millisecondsSinceEpoch,
        healthScore: result.healthScore,
        healthSummary: result.summary,
        healthDetails: {
          "complexion": result.details.complexion,
          "eyes": result.details.eyes,
          "spirit": result.details.spirit,
          "skin": result.details.skin,
          "bodyShape": result.details.bodyShape,
          "overallAssessment": result.details.overallAssessment,
          "concerns": result.details.concerns,
          "strengths": result.details.strengths,
        },
        comparisonJson: comparison?.summary ?? "",
        improvementStatus: comparison?.status ?? "stable",
      );
      await _db.saveRecord(record);

      // Update person stats
      final updated = person.copyWith(
        healthScore: result.healthScore,
        recordCount: person.recordCount + 1,
        lastPhotoPath: photo.path,
        lastAnalyzedAt: DateTime.now().millisecondsSinceEpoch,
      );
      await _db.savePerson(updated);
      final idx = _persons.indexWhere((p) => p.personId == updated.personId);
      if (idx >= 0) _persons[idx] = updated;

      // Save plans
      final plans = result.actionPlans.asMap().entries.map((e) {
        final plan = ActionPlanModel(
          planId: DatabaseService.generateUUID(),
          personId: person.personId,
          recordId: record.recordId,
          title: e.value.title,
          description: e.value.description,
          frequency: e.value.frequency,
          sortOrder: e.key,
        );
        return plan;
      }).toList();
      await _db.savePlans(plans);

      final items = <ActionItemModel>[];
      for (var i = 0; i < result.actionPlans.length; i++) {
        for (var j = 0; j < result.actionPlans[i].items.length; j++) {
          items.add(ActionItemModel(
            itemId: DatabaseService.generateUUID(),
            planId: plans[i].planId,
            content: result.actionPlans[i].items[j].content,
            sortOrder: j,
          ));
        }
      }
      await _db.saveItems(items);

      _isAnalyzing = false;
      _selectedPerson = updated;
      notifyListeners();

      return CaptureFlowResult(
        isNewPerson: isNew,
        person: updated,
        hasComparison: comparison != null,
      );
    } catch (e) {
      _error = e.toString();
      _isAnalyzing = false;
      notifyListeners();
      return null;
    }
  }

  // Records
  Future<void> loadRecords(String personId) async {
    _records = await _db.getRecords(personId);
    notifyListeners();
  }

  Future<HealthRecordModel?> getLatestRecord(String personId) async {
    return _db.getLatestRecord(personId);
  }

  // Plans
  Future<void> loadPlans(String personId) async {
    _plans = await _db.getActivePlans(personId);
    notifyListeners();
  }

  Future<List<ActionItemModel>> getPlanItems(String planId) async {
    return _db.getItems(planId);
  }

  Future<void> toggleItem(ActionItemModel item) async {
    final updated = item.copyWith(isChecked: !item.isChecked, checkedAt: DateTime.now().millisecondsSinceEpoch);
    await _db.updateItem(updated);
    notifyListeners();
  }

  void clearError() { _error = null; notifyListeners(); }
}

class CaptureFlowResult {
  final bool isNewPerson;
  final PersonModel person;
  final bool hasComparison;
  CaptureFlowResult({required this.isNewPerson, required this.person, required this.hasComparison});
}
