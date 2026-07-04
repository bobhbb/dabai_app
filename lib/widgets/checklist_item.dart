import "package:flutter/material.dart";
import "../config/constants.dart";

class ChecklistItem extends StatelessWidget {
  final String content;
  final bool isChecked;
  final VoidCallback onToggle;

  const ChecklistItem({
    super.key,
    required this.content,
    required this.isChecked,
    required this.onToggle,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onToggle,
      child: Padding(
        padding: const EdgeInsets.symmetric(vertical: 4),
        child: Row(
          children: [
            Icon(
              isChecked ? Icons.check_circle : Icons.radio_button_unchecked,
              color: isChecked ? const Color(DabaiColors.healthGood) : const Color(DabaiColors.subtext),
              size: 22,
            ),
            const SizedBox(width: 8),
            Expanded(
              child: Text(
                content,
                style: TextStyle(
                  fontSize: 14,
                  color: isChecked ? const Color(DabaiColors.subtext) : const Color(DabaiColors.onSurface),
                  decoration: isChecked ? TextDecoration.lineThrough : null,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
