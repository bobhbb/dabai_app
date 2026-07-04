package com.dabai.app.ui.screens.report
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dabai.app.ui.theme.*
import com.dabai.app.ui.components.HealthScoreBar
@Composable
fun HealthReportScreen(personId: String, recordId: String, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(DabaiBackground)) {
        TopAppBar(title = { Text("健康报告", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "返回") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = DabaiBackground))
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), contentPadding = PaddingValues(16.dp)) {
            ScoreOverviewCard(score = 78f, previousScore = 72f)
            Spacer(modifier = Modifier.height(16.dp))
            ComparisonCard(status = "improved", scoreDiff = 6f, improvements = listOf("面部气色较上次红润", "精神状态评分提升", "整体健康趋势向好"), regressions = emptyList())
            Spacer(modifier = Modifier.height(16.dp))
            Text("详细分析", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            DetailItem("面色", "面色红润，气血充足")
            DetailItem("眼神", "眼神明亮有神，精神状态良好")
            DetailItem("皮肤", "皮肤弹性较好，建议注意保湿")
            DetailItem("体型", "体型匀称，继续保持")
            DetailItem("总体评估", "整体健康状况良好，保持良好的生活习惯可进一步提升")
        }
    }
}
@Composable
private fun ScoreOverviewCard(score: Float, previousScore: Float) {
    val color = when { score >= 75 -> HealthGood; score >= 55 -> HealthWarn; else -> HealthBad }
    Surface(shape = RoundedCornerShape(20.dp), color = DabaiSurface, elevation = SurfaceDefaults.elevation(defaultElevation = 2.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("综合健康评分", fontSize = 14.sp, color = DabaiSubtext)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${score.toInt()}", fontSize = 64.sp, fontWeight = FontWeight.Bold, color = color)
            Text(text = "分", fontSize = 18.sp, color = DabaiSubtext)
            Spacer(modifier = Modifier.height(12.dp))
            HealthScoreBar(score = score, color = color, showText = false)
            Spacer(modifier = Modifier.height(8.dp))
            val diff = score - previousScore
            if (diff > 0) { Text("较上次提升 +${"%.0f".format(diff)} 分", fontSize = 13.sp, color = HealthGood, fontWeight = FontWeight.Medium) }
            else if (diff < 0) { Text("较上次下降 ${"%.0f".format(diff)} 分", fontSize = 13.sp, color = HealthBad, fontWeight = FontWeight.Medium) }
            else { Text("与上次持平", fontSize = 13.sp, color = DabaiSubtext) }
        }
    }
}
@Composable
private fun ComparisonCard(status: String, scoreDiff: Float, improvements: List<String>, regressions: List<String>) {
    val (icon, statusText, statusColor) = when (status) {
        "improved" -> Triple("📈", "有改善", HealthGood)
        "declined" -> Triple("📉", "需关注", HealthBad)
        else -> Triple("➡️", "保持稳定", DabaiSubtext)
    }
    Surface(shape = RoundedCornerShape(20.dp), color = DabaiSurface, elevation = SurfaceDefaults.elevation(defaultElevation = 2.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) { Text("$icon  ", fontSize = 16.sp); Text("对比上次报告", fontSize = 16.sp, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.weight(1f)); Surface(shape = RoundedCornerShape(12.dp), color = statusColor.copy(alpha = 0.15f)) { Text(" $statusText ", modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp), fontSize = 12.sp, color = statusColor, fontWeight = FontWeight.Medium) } }
            Spacer(modifier = Modifier.height(12.dp))
            improvements.forEach { item -> Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) { Text("✅", fontSize = 14.sp); Spacer(modifier = Modifier.width(8.dp)); Text(item, fontSize = 14.sp, color = DabaiOnSurface) } }
            regressions.forEach { item -> Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) { Text("⚠️", fontSize = 14.sp); Spacer(modifier = Modifier.width(8.dp)); Text(item, fontSize = 14.sp, color = HealthBad) } }
        }
    }
}
@Composable
private fun DetailItem(label: String, value: String) {
    Surface(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp), shape = RoundedCornerShape(12.dp), color = DabaiSurface) {
        Row(modifier = Modifier.padding(16.dp)) { Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DabaiSubtext, modifier = Modifier.width(72.dp)); Spacer(modifier = Modifier.width(12.dp)); Text(text = value, fontSize = 14.sp, color = DabaiOnSurface) }
    }
}
