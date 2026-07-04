package com.dabai.app.ui.screens.todo
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.dabai.app.ui.components.ChecklistItem
@Composable
fun TodoScreen(personId: String, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(DabaiBackground)) {
        TopAppBar(title = { Text("行动计划", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "返回") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = DabaiBackground))
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 8.dp)) {
            item { DailyPlanSection() }
            item { WeeklyPlanSection() }
            item { MonthlyPlanSection() }
            item { YearlyPlanSection() }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}
@Composable
private fun DailyPlanSection() {
    val todayItems = remember { mutableStateListOf(false to "早起喝一杯温水", true to "保持30分钟户外活动", false to "记录饮食和心情", false to "睡前放松冥想5分钟") }
    PlanCardSection(title = "☀️ 今日习惯", frequency = "每日", total = todayItems.size, done = todayItems.count { it.second }) {
        todayItems.forEachIndexed { index, (isChecked, content) -> ChecklistItem(content = content, isChecked = isChecked, onToggle = { todayItems[index] = !isChecked to content }) }
    }
}
@Composable
private fun WeeklyPlanSection() {
    PlanCardSection(title = "📅 每周健康目标", frequency = "每周", total = 4, done = 2) {
        ChecklistItem(content = "晨间散步30分钟", isChecked = true, onToggle = { })
        ChecklistItem(content = "测量体重并记录", isChecked = true, onToggle = { })
        ChecklistItem(content = "与家人一起做一件开心的事", isChecked = false, onToggle = { })
        ChecklistItem(content = "回顾本周健康习惯", isChecked = false, onToggle = { })
    }
}
@Composable
private fun MonthlyPlanSection() {
    PlanCardSection(title = "📊 每月健康回顾", frequency = "每月", total = 3, done = 0) {
        ChecklistItem(content = "拍照更新健康报告", isChecked = false, onToggle = { })
        ChecklistItem(content = "回顾本月健康习惯完成情况", isChecked = false, onToggle = { })
        ChecklistItem(content = "调整下月健康计划", isChecked = false, onToggle = { })
    }
}
@Composable
private fun YearlyPlanSection() {
    PlanCardSection(title = "🎯 年度健康目标", frequency = "每年", total = 2, done = 1) {
        ChecklistItem(content = "培养1-2个新的健康习惯", isChecked = true, onToggle = { })
        ChecklistItem(content = "定期进行全面健康检查", isChecked = false, onToggle = { })
    }
}
@Composable
private fun PlanCardSection(title: String, frequency: String, total: Int, done: Int, content: @Composable ColumnScope.() -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), shape = RoundedCornerShape(20.dp), color = DabaiSurface, elevation = SurfaceDefaults.elevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column { Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold); Text("$done/$total", fontSize = 12.sp, color = DabaiSubtext) }
                Surface(shape = RoundedCornerShape(16.dp), color = DabaiPrimaryLight) { Text(frequency, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 12.sp, color = DabaiPrimaryDark, fontWeight = FontWeight.Medium) }
            }
            LinearProgressIndicator(progress = { done.toFloat() / total }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(6.dp), color = HealthGood, trackColor = DabaiDivider)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
