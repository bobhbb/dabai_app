import "dart:io";
import "dart:convert";
import "package:image_picker/image_picker.dart";
import "package:crypto/crypto.dart";
import "../models/person_model.dart";
import "database_service.dart";

class CameraService {
  static final CameraService _instance = CameraService._();
  factory CameraService() => _instance;
  CameraService._();

  final ImagePicker _picker = ImagePicker();

  Future<XFile?> takePhoto() async {
    return await _picker.pickImage(source: ImageSource.camera, imageQuality: 85, maxWidth: 1280);
  }

  Future<XFile?> pickFromGallery() async {
    return await _picker.pickImage(source: ImageSource.gallery, imageQuality: 85, maxWidth: 1280);
  }
}

class FaceService {
  static final FaceService _instance = FaceService._();
  factory FaceService() => _instance;
  FaceService._();

  Future<FaceResult> analyze(String imagePath) async {
    final bytes = File(imagePath).readAsBytesSync();
    final hash = sha256.convert(bytes.take(8192)).toString();
    final db = DatabaseService();
    final existing = await db.getPersonByFaceToken(hash);
    return FaceResult(
      faceToken: hash,
      isKnown: existing != null,
      matchedPerson: existing,
    );
  }
}

class FaceResult {
  final String faceToken;
  final bool isKnown;
  final PersonModel? matchedPerson;
  FaceResult({required this.faceToken, required this.isKnown, this.matchedPerson});
}
