import 'package:flutter/material.dart';
import 'constants.dart';

class DabaiTheme {
  static ThemeData get lightTheme {
    return ThemeData(
      useMaterial3: true,
      colorScheme: ColorScheme.fromSeed(
        seedColor: const Color(DabaiColors.primary),
        primary: const Color(DabaiColors.primary),
        secondary: const Color(DabaiColors.secondary),
        surface: const Color(DabaiColors.surface),
        brightness: Brightness.light,
      ),
      scaffoldBackgroundColor: const Color(DabaiColors.background),
      appBarTheme: const AppBarTheme(
        backgroundColor: Color(DabaiColors.background),
        foregroundColor: Color(DabaiColors.onSurface),
        elevation: 0,
        centerTitle: true,
      ),
      cardTheme: CardThemeData(
        elevation: 2,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
        color: const Color(DabaiColors.surface),
      ),
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          backgroundColor: const Color(DabaiColors.primary),
          foregroundColor: Colors.white,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24)),
          padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 14),
        ),
      ),
      textTheme: const TextTheme(
        headlineLarge: TextStyle(fontSize: 28, fontWeight: FontWeight.bold, color: Color(DabaiColors.onSurface)),
        headlineMedium: TextStyle(fontSize: 22, fontWeight: FontWeight.bold, color: Color(DabaiColors.onSurface)),
        titleLarge: TextStyle(fontSize: 18, fontWeight: FontWeight.w600, color: Color(DabaiColors.onSurface)),
        titleMedium: TextStyle(fontSize: 16, fontWeight: FontWeight.w500, color: Color(DabaiColors.onSurface)),
        bodyLarge: TextStyle(fontSize: 16, color: Color(DabaiColors.onSurface)),
        bodyMedium: TextStyle(fontSize: 14, color: Color(DabaiColors.onSurface)),
        bodySmall: TextStyle(fontSize: 12, color: Color(DabaiColors.subtext)),
      ),
    );
  }
}
