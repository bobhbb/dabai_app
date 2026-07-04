import "package:flutter/material.dart";
import "../config/constants.dart";

class HealthScoreBar extends StatelessWidget {
  final double score;
  final Color color;
  final bool showText;
  final double height;

  const HealthScoreBar({
    super.key,
    required this.score,
    this.color = const Color(DabaiColors.healthGood),
    this.showText = true,
    this.height = 6,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        if (showText) ...[
          Text(
            "${score.toInt()}分",
            style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold, color: color),
          ),
          const SizedBox(height: 4),
        ],
        ClipRRect(
          borderRadius: BorderRadius.circular(3),
          child: LinearProgressIndicator(
            value: score / 100,
            minHeight: height,
            backgroundColor: const Color(DabaiColors.divider),
            valueColor: AlwaysStoppedAnimation<Color>(color),
          ),
        ),
      ],
    );
  }
}
