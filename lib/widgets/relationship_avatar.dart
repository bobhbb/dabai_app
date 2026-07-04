import "package:flutter/material.dart";
import "../config/constants.dart";

class RelationshipAvatar extends StatelessWidget {
  final String relationship;
  final String nickname;
  final double size;
  final double fontSize;

  const RelationshipAvatar({
    super.key,
    required this.relationship,
    required this.nickname,
    this.size = 48,
    this.fontSize = 20,
  });

  Color get backgroundColor {
    switch (relationship) {
      case "爸爸": case "父亲": return const Color(DabaiColors.relationshipDad);
      case "妈妈": case "母亲": return const Color(DabaiColors.relationshipMom);
      case "爷爷": case "外公": return const Color(DabaiColors.relationshipGrandpa);
      case "奶奶": case "外婆": return const Color(DabaiColors.relationshipGrandma);
      case "哥哥": case "弟弟": return const Color(DabaiColors.relationshipBrother);
      case "姐姐": case "妹妹": return const Color(DabaiColors.relationshipSister);
      default: return const Color(DabaiColors.relationshipDefault);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      width: size, height: size,
      decoration: BoxDecoration(shape: BoxShape.circle, color: backgroundColor),
      child: Center(
        child: Text(
          nickname.isNotEmpty ? nickname.characters.first : "?",
          style: TextStyle(color: Colors.white, fontSize: fontSize, fontWeight: FontWeight.bold),
        ),
      ),
    );
  }
}
