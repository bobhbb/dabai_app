import "package:flutter/material.dart";
import "package:provider/provider.dart";
import "providers/app_state.dart";
import "config/theme.dart";
import "screens/splash_screen.dart";
import "screens/login_screen.dart";
import "screens/home_screen.dart";
import "screens/camera_screen.dart";
import "screens/person_detail_screen.dart";
import "screens/health_report_screen.dart";
import "screens/todo_screen.dart";

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(
    ChangeNotifierProvider(
      create: (_) => AppState()..initialize(),
      child: const DabaiApp(),
    ),
  );
}

class DabaiApp extends StatelessWidget {
  const DabaiApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "\u5927\u767d",
      theme: DabaiTheme.lightTheme,
      debugShowCheckedModeBanner: false,
      initialRoute: "/",
      onGenerateRoute: (settings) {
        Widget page;
        switch (settings.name) {
          case "/login":
            page = const LoginScreen();
            break;
          case "/home":
            page = const HomeScreen();
            break;
          case "/camera":
            page = const CameraScreen();
            break;
          case "/person-detail":
            final args = settings.arguments as Map<String, dynamic>;
            page = PersonDetailScreen(personId: args["personId"]);
            break;
          case "/health-report":
            final args = settings.arguments as Map<String, dynamic>;
            page = HealthReportScreen(personId: args["personId"], recordId: args["recordId"]);
            break;
          case "/todo":
            final args = settings.arguments as Map<String, dynamic>;
            page = TodoScreen(personId: args["personId"]);
            break;
          default:
            page = const SplashScreen();
        }
        return MaterialPageRoute(builder: (_) => page, settings: settings);
      },
    );
  }
}
