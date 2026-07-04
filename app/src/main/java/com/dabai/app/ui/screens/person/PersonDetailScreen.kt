package com.dabai.app.ui.screens.person
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dabai.app.ui.theme.*
import com.dabai.app.ui.components.HealthScoreBar
import com.dabai.app.ui.components.RelationshipAvatar
@Composable
fun PersonDetailScreen(personId: String, onTakePhoto: () -> Unit, onViewReport: (String) -> Unit, onViewTodo: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(DabaiBackground)) {
        TopAppBar(title = { }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "返回") } }, actions = { IconButton(onClick = onTakePhoto) { Icon(Icons.Filled.CameraAlt, "拍照") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = DabaiBackground))
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 32.dp)) {
            item { PersonInfoCard(nickname = "老妈", relationship = "妈妈", healthScore = 78f, recordCount = 5) }
            item { Spacer(modifier = Modifier.height(16.dp)); ActionButtons(onViewTodo = onViewTodo, onTakePhoto = onTakePhoto) }
            item { Spacer(modifier = Modifier.height(8.dp)); Text("健康记录", modifier = Modifier.padding(horizontal = 20.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(8.dp)) }
            items(5) { index -> HealthHistoryCard(index = index, onClick = { onViewReport("record_$index") }) }
        }
    }
}
@Composable
private fun PersonInfoCard(nickname: String, relationship: String, healthScore: Float, recordCount: Int) {
    Surface(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(20.dp), color = DabaiSurface, elevation = SurfaceDefaults.elevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            RelationshipAvatar(relationship = relationship, nickname = nickname, size = 72.dp)
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = nickname, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(text = relationship, fontSize = 14.sp, color = DabaiSubtext)
                Text(text = "已检测 $recordCount 次", fontSize = 12.sp, color = DabaiSubtext)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${healthScore.toInt()}", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = HealthGood)
                Text(text = "分", fontSize = 14.sp, color = DabaiSubtext)
            }
        }
    }
}
@Composable
private fun ActionButtons(onViewTodo: () -> Unit, onTakePhoto: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        OutlinedButton(onClick = onViewTodo, modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(12.dp)) { Icon(Icons.Filled.Checklist, "待办", modifier = Modifier.size(20.dp)); Spacer(modifier = Modifier.width(6.dp)); Text("行动计划") }
        Spacer(modifier = Modifier.width(12.dp))
        Button(onClick = onTakePhoto, modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = DabaiPrimary)) { Icon(Icons.Filled.CameraAlt, "拍照", modifier = Modifier.size(20.dp)); Spacer(modifier = Modifier.width(6.dp)); Text("重新检测") }
    }
}
@Composable
private fun HealthHistoryCard(index: Int, onClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), shape = RoundedCornerShape(12.dp), color = DabaiSurface, onClick = onClick) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(DabaiPrimaryLight, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) { Icon(Icons.Filled.Description, "报告", tint = DabaiPrimary, modifier = Modifier.size(22.dp)) }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) { Text("第 ${index + 1} 次健康报告", fontWeight = FontWeight.Medium); Text("${System.currentTimeMillis() - index * 86400000L}", fontSize = 12.sp, color = DabaiSubtext) }
            Icon(Icons.Filled.ChevronRight, "查看", tint = DabaiSubtext)
        }
    }
}
