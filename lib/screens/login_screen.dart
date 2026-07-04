import "package:flutter/material.dart";
import "package:provider/provider.dart";
import "../providers/app_state.dart";
import "../config/constants.dart";

class LoginScreen extends StatelessWidget {
  const LoginScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(DabaiColors.background),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(32),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Text("🤖", style: TextStyle(fontSize: 100)),
              const SizedBox(height: 24),
              const Text("大白伴家", style: TextStyle(fontSize: 36, fontWeight: FontWeight.bold, letterSpacing: 6, color: Color(DabaiColors.onPrimary))),
              const SizedBox(height: 8),
              Text(AppStrings.welcomeSubtitle, style: const TextStyle(fontSize: 16, color: Color(DabaiColors.subtext), letterSpacing: 2)),
              const SizedBox(height: 40),
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(20),
                  child: Column(
                    children: [
                      _FeatureRow("📸", "拍照即健康分析", "拍张照片，AI自动分析家人健康状态"),
                      const SizedBox(height: 12),
                      _FeatureRow("📊", "持续健康追踪", "每次拍照对比上次，看改善效果"),
                      const SizedBox(height: 12),
                      _FeatureRow("✅", "个性化行动计划", "每日/周/月/年待办清单，轻松打卡"),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 48),
              SizedBox(
                width: double.infinity,
                height: 56,
                child: ElevatedButton(
                  onPressed: () async {
                    final state = context.read<AppState>();
                    await state.login();
                    if (context.mounted) {
                      Navigator.pushReplacementNamed(context, "/home");
                    }
                  },
                  child: const Text("微信登录", style: TextStyle(fontSize: 18)),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _FeatureRow extends StatelessWidget {
  final String icon, title, desc;
  const _FeatureRow(this.icon, this.title, this.desc);
  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Text(icon, style: const TextStyle(fontSize: 28)),
        const SizedBox(width: 16),
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(title, style: const TextStyle(fontSize: 15, fontWeight: FontWeight.w500)),
              Text(desc, style: const TextStyle(fontSize: 12, color: Color(DabaiColors.subtext))),
            ],
          ),
        ),
      ],
    );
  }
}
