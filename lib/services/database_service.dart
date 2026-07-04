import 'dart:convert';
import 'dart:io';
import 'package:path_provider/path_provider.dart';
import 'package:uuid/uuid.dart';
import "package:crypto/crypto.dart";
import "../models/user_model.dart";
import "../models/person_model.dart";
import "../models/health_record_model.dart";
import "../models/action_plan_model.dart";

class DatabaseService {
  static final DatabaseService _instance = DatabaseService._();
  factory DatabaseService() => _instance;
  DatabaseService._();

  String? _basePath;

  Future<String> get _dbPath async {
    if (_basePath != null) return _basePath!;
    final dir = await getApplicationDocumentsDirectory();
    _basePath = "${dir.path}/dabai_data";
    await Directory(_basePath!).create(recursive: true);
    return _basePath!;
  }

  Future<T> _readJson<T>(String file, T Function(Map<String, dynamic>) fromJson, {T? Function()? fallback}) async {
    final path = "${await _dbPath}/$file";
    final fileObj = File(path);
    if (!await fileObj.exists()) {
      if (fallback != null) return fallback() as T;
      throw Exception("File not found: $file");
    }
    final content = await fileObj.readAsString();
    return fromJson(jsonDecode(content));
  }

  Future<List<T>> _readJsonList<T>(String file, T Function(Map<String, dynamic>) fromJson) async {
    final path = "${await _dbPath}/$file";
    final fileObj = File(path);
    if (!await fileObj.exists()) return [];
    final content = await fileObj.readAsString();
    final list = jsonDecode(content) as List;
    return list.map((e) => fromJson(e)).toList();
  }

  Future<void> _writeJson(String file, dynamic data) async {
    final path = "${await _dbPath}/$file";
    final fileObj = File(path);
    await fileObj.writeAsString(jsonEncode(data));
  }

  // ========== User ==========
  Future<UserModel?> getUser() async {
    try {
      return await _readJson("user.json", UserModel.fromJson);
    } catch (_) {
      return null;
    }
  }

  Future<void> saveUser(UserModel user) async {
    await _writeJson("user.json", user.toJson());
  }

  // ========== Persons ==========
  Future<List<PersonModel>> getPersons() async {
    return _readJsonList("persons.json", PersonModel.fromJson);
  }

  Future<int> getPersonCount() async {
    final persons = await getPersons();
    return persons.length;
  }

  Future<PersonModel?> getPersonById(String personId) async {
    final persons = await getPersons();
    try { return persons.firstWhere((p) => p.personId == personId); } catch (_) { return null; }
  }

  Future<PersonModel?> getPersonByFaceToken(String faceToken) async {
    final persons = await getPersons();
    try { return persons.firstWhere((p) => p.faceToken == faceToken); } catch (_) { return null; }
  }

  Future<void> savePerson(PersonModel person) async {
    final persons = await getPersons();
    final idx = persons.indexWhere((p) => p.personId == person.personId);
    if (idx >= 0) {
      persons[idx] = person;
    } else {
      persons.add(person);
    }
    await _writeJson("persons.json", persons.map((e) => e.toJson()).toList());
  }

  Future<void> deletePerson(String personId) async {
    final persons = await getPersons();
    persons.removeWhere((p) => p.personId == personId);
    await _writeJson("persons.json", persons.map((e) => e.toJson()).toList());
  }

  // ========== Health Records ==========
  Future<List<HealthRecordModel>> getRecords(String personId) async {
    final all = await _readJsonList("records.json", HealthRecordModel.fromJson);
    all.sort((a, b) => b.analyzedAt.compareTo(a.analyzedAt));
    return all.where((r) => r.personId == personId).toList();
  }

  Future<HealthRecordModel?> getLatestRecord(String personId) async {
    final records = await getRecords(personId);
    return records.isNotEmpty ? records.first : null;
  }

  Future<HealthRecordModel?> getSecondLatestRecord(String personId) async {
    final records = await getRecords(personId);
    return records.length > 1 ? records[1] : null;
  }

  Future<void> saveRecord(HealthRecordModel record) async {
    final all = await _readJsonList("records.json", HealthRecordModel.fromJson);
    all.add(record);
    await _writeJson("records.json", all.map((e) => e.toJson()).toList());
  }

  // ========== Action Plans ==========
  Future<List<ActionPlanModel>> getPlans(String personId) async {
    final all = await _readJsonList("plans.json", ActionPlanModel.fromJson);
    all.sort((a, b) => a.sortOrder.compareTo(b.sortOrder));
    return all.where((p) => p.personId == personId).toList();
  }

  Future<List<ActionPlanModel>> getActivePlans(String personId) async {
    final plans = await getPlans(personId);
    return plans.where((p) => !p.isCompleted).toList();
  }

  Future<void> savePlans(List<ActionPlanModel> plans) async {
    final all = await _readJsonList("plans.json", ActionPlanModel.fromJson);
    all.addAll(plans);
    await _writeJson("plans.json", all.map((e) => e.toJson()).toList());
  }

  Future<void> updatePlan(ActionPlanModel plan) async {
    final all = await _readJsonList("plans.json", ActionPlanModel.fromJson);
    final idx = all.indexWhere((p) => p.planId == plan.planId);
    if (idx >= 0) all[idx] = plan;
    await _writeJson("plans.json", all.map((e) => e.toJson()).toList());
  }

  // ========== Action Items ==========
  Future<List<ActionItemModel>> getItems(String planId) async {
    final all = await _readJsonList("items.json", ActionItemModel.fromJson);
    all.sort((a, b) => a.sortOrder.compareTo(b.sortOrder));
    return all.where((i) => i.planId == planId).toList();
  }

  Future<void> saveItems(List<ActionItemModel> items) async {
    final all = await _readJsonList("items.json", ActionItemModel.fromJson);
    all.addAll(items);
    await _writeJson("items.json", all.map((e) => e.toJson()).toList());
  }

  Future<void> updateItem(ActionItemModel item) async {
    final all = await _readJsonList("items.json", ActionItemModel.fromJson);
    final idx = all.indexWhere((i) => i.itemId == item.itemId);
    if (idx >= 0) all[idx] = item;
    await _writeJson("items.json", all.map((e) => e.toJson()).toList());
  }

  // ========== Face Token ==========
  static String generateFaceToken(String imagePath) {
    final bytes = File(imagePath).readAsBytesSync();
    final hash = sha256.convert(bytes.take(8192));
    return hash.toString();
  }

  static String generateUUID() => const Uuid().v4();
}
