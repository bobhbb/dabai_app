import "package:flutter/material.dart";
import "package:provider/provider.dart";
import "../providers/app_state.dart";
import "../config/constants.dart";

class CameraScreen extends StatelessWidget {
  const CameraScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final state = context.watch<AppState>();
    return Scaffold(
      backgroundColor: Colors.black,
      appBar: AppBar(
        backgroundColor: Colors.black,
        foregroundColor: Colors.white,
        elevation: 0,
        title: const Text("拍摄家人照片"),
        leading: IconButton(icon: const Icon(Icons.close), onPressed: () => Navigator.pop(context)),
      ),
      body: Column(
        children: [
          Expanded(
            child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Container(
                    width: 280, height: 280,
                    decoration: BoxDecoration(
                      border: Border.all(color: Colors.white.withOpacity(0.5), width: 2),
                      borderRadius: BorderRadius.circular(16),
                    ),
                    child: Center(
                      child: Text(
                        state.isAnalyzing ? "" : "将人脸对准框内\n保持光线充足",
                        textAlign: TextAlign.center,
                        style: TextStyle(color: Colors.white.withOpacity(0.7), fontSize: 14),
                      ),
                    ),
                  ),
                  if (state.error != null) ...[
                    const SizedBox(height: 16),
                    Text(state.error!, style: const TextStyle(color: Colors.red, fontSize: 14)),
                    TextButton(onPressed: () => state.clearError(), child: const Text("重试", style: TextStyle(color: Colors.white))),
                  ],
                ],
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(bottom: 48),
            child: state.isAnalyzing
                ? const Column(
                    children: [
                      CircularProgressIndicator(color: Colors.white),
                      SizedBox(height: 12),
                      Text("AI 分析中...", style: TextStyle(color: Colors.white, fontSize: 14)),
                    ],
                  )
                : FloatingActionButton(
                    onPressed: () async {
                      final result = await state.captureAndAnalyze();
                      if (result != null && context.mounted) {
                        Navigator.pushReplacementNamed(
                          context,
                          "/person-detail",
                          arguments: {"personId": result.person.personId},
                        );
                      }
                    },
                    backgroundColor: Colors.white,
                    child: const Icon(Icons.camera_alt, size: 40, color: Color(DabaiColors.primary)),
                  ),
          ),
        ],
      ),
    );
  }
}
