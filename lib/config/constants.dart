class DabaiColors {
  static const primary = 0xFF4FC3F7;
  static const primaryDark = 0xFF0288D1;
  static const primaryLight = 0xFFB3E5FC;
  static const secondary = 0xFF81C784;
  static const background = 0xFFF0F4F8;
  static const surface = 0xFFFFFFFF;
  static const onPrimary = 0xFF1A1A2E;
  static const onSurface = 0xFF2D3436;
  static const subtext = 0xFF636E72;
  static const divider = 0xFFE0E0E0;
  static const healthGood = 0xFF00B894;
  static const healthWarn = 0xFFFDCB6E;
  static const healthBad = 0xFFE17055;
  static const relationshipDad = 0xFF4A90D9;
  static const relationshipMom = 0xFFE87EA4;
  static const relationshipGrandpa = 0xFF8D6E63;
  static const relationshipGrandma = 0xFFCE93D8;
  static const relationshipBrother = 0xFF26A69A;
  static const relationshipSister = 0xFFEC407A;
  static const relationshipDefault = 0xFF78909C;
}

class AppStrings {
  static const appName = "大白";
  static const appSubtitle = "家庭健康陪伴";
  static const welcomeTitle = "大白伴家";
  static const welcomeSubtitle = "用AI守护家人健康";
  static const loginWechat = "微信登录";
  static const takePhoto = "拍照";
  static const analyzing = "正在分析...";
  static const healthReport = "健康报告";
  static const actionPlan = "行动计划";
  static const addMember = "添加家人";
  static const memberLimit = "已达3人上限";
  static const todayPlan = "今日计划";
  static const permissionCamera = "需要相机权限来拍摄家人照片用于健康分析";
  static const grantPermission = "授予权限";

  static const relationships = ["爸爸", "妈妈", "爷爷", "奶奶", "哥哥", "姐姐", "妹妹", "弟弟"];

  static const Map<String, List<String>> nicknames = {
    "爸爸": ["老爸", "老爹", "阿爸", "爸比"],
    "妈妈": ["老妈", "娘亲", "阿妈", "妈咪"],
    "爷爷": ["爷爷", "阿公", "老爷爷"],
    "奶奶": ["奶奶", "阿婆", "老奶奶"],
    "哥哥": ["老哥", "大哥", "阿哥", "哥仔"],
    "姐姐": ["老姐", "大姐", "阿姐"],
    "妹妹": ["小妹", "老妹", "阿妹"],
    "弟弟": ["老弟", "小弟", "阿弟"],
  };

  static String getNickname(String relationship) {
    final list = nicknames[relationship];
    if (list == null || list.isEmpty) return "家人";
    return (list..shuffle()).first;
  }
}

class AppRoutes {
  static const String splash = "/";
  static const String login = "/login";
  static const String home = "/home";
  static const String camera = "/camera";
  static const String personDetail = "/person-detail";
  static const String healthReport = "/health-report";
  static const String todo = "/todo";
}
