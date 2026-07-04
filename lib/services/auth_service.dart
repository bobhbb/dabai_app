import "package:shared_preferences/shared_preferences.dart";
import "../models/user_model.dart";
import "database_service.dart";

class AuthService {
  static final AuthService _instance = AuthService._();
  factory AuthService() => _instance;
  AuthService._();

  UserModel? _currentUser;
  bool _isLoggedIn = false;

  bool get isLoggedIn => _isLoggedIn;
  UserModel? get currentUser => _currentUser;

  Future<void> init() async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getString("user_id");
    if (userId != null) {
      final db = DatabaseService();
      final user = await db.getUser();
      if (user != null) {
        _currentUser = user;
        _isLoggedIn = true;
      }
    }
  }

  Future<UserModel> loginWithWeChat() async {
    final db = DatabaseService();
    final existing = await db.getUser();
    if (existing != null) {
      _currentUser = existing;
      _isLoggedIn = true;
      return existing;
    }

    final user = UserModel(
      userId: DatabaseService.generateUUID(),
      nickname: "大白用户",
    );
    await db.saveUser(user);
    _currentUser = user;
    _isLoggedIn = true;

    final prefs = await SharedPreferences.getInstance();
    await prefs.setString("user_id", user.userId);

    return user;
  }

  Future<void> logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove("user_id");
    _currentUser = null;
    _isLoggedIn = false;
  }
}
