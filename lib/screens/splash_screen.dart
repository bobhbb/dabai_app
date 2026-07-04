import "package:flutter/material.dart";
import "package:provider/provider.dart";
import "../providers/app_state.dart";

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});
  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  void initState() {
    super.initState();
    _checkAuth();
  }

  Future<void> _checkAuth() async {
    final state = context.read<AppState>();
    await Future.delayed(const Duration(milliseconds: 1500));
    if (!mounted) return;

    if (state.isLoggedIn) {
      Navigator.pushReplacementNamed(context, "/home");
    } else {
      Navigator.pushReplacementNamed(context, "/login");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF4FC3F7),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text("🤖", style: TextStyle(fontSize: 80)),
            const SizedBox(height: 16),
            const Text("大白", style: TextStyle(fontSize: 48, fontWeight: FontWeight.bold, color: Colors.white, letterSpacing: 8)),
            const SizedBox(height: 8),
            Text("家庭健康陪伴", style: TextStyle(fontSize: 18, color: Colors.white.withOpacity(0.8), letterSpacing: 4)),
            const SizedBox(height: 60),
            const SizedBox(width: 32, height: 32, child: CircularProgressIndicator(color: Colors.white, strokeWidth: 2)),
          ],
        ),
      ),
    );
  }
}
