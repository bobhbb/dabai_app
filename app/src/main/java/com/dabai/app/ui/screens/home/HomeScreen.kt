package com.dabai.app.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dabai.app.data.local.entity.PersonEntity
import com.dabai.app.data.local.entity.UserEntity
import com.dabai.app.ui.theme.*
import com.dabai.app.ui.components.HealthScoreBar
import com.dabai.app.ui.components.RelationshipAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCameraClick: (String) -> Unit,
    onPersonClick: (String) -> Unit
) {
    // 模拟数据：实际项目中通过 ViewModel + Hilt 注入仓库获取
    val mockPersons = remember {
        listOf(
            PersonEntity(
                personId = "1", userId = "u1", faceToken = "t1",
                relationship = "妈妈", nickname = "老妈",
                avatarPath = "", healthScore = 78f, recordCount = 5,
                lastPhotoPath = "", lastAnalyzedAt = System.currentTimeMillis() - 86400000,
                createdAt = System.currentTimeMillis() - 2592000000
            ),
            PersonEntity(
                personId = "2", userId = "u1", faceToken = "t2",
                relationship = "爸爸", nickname = "老爸",
                avatarPath = "", healthScore = 82f, recordCount = 3,
                lastPhotoPath = "", lastAnalyzedAt = System.currentTimeMillis() - 172800000,
                createdAt = System.currentTimeMillis() - 1814400000
            ),
            PersonEntity(
                personId = "3", userId = "u1", faceToken = "t3",
                relationship = "奶奶", nickname = "奶奶",
                avatarPath = "", healthScore = 65f, recordCount = 2,
                lastPhotoPath = "", lastAnalyzedAt = System.currentTimeMillis() - 345600000,
                createdAt = System.currentTimeMillis() - 864000000
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DabaiBackground)
    ) {
        // 顶部栏
        HomeTopBar(nickname = "小明", personCount = mockPersons.size)

        // 可用席位
        if (mockPersons.size < 3) {
            SeatIndicator(used = mockPersons.size, total = 3)
        }

        // 家人列表
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(mockPersons) { person ->
                PersonCard(
                    person = person,
                    onClick = { onPersonClick(person.personId) },
                    onTakePhoto = {
                        onCameraClick("photo_${person.personId}")
                    }
                )
            }

            // 添加家人按钮（未满3人）
            if (mockPersons.size < 3) {
                item {
                    AddPersonCard(onClick = { onCameraClick("new") })
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 底部拍照按钮
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = { onCameraClick("normal") },
                modifier = Modifier.size(80.dp),
                containerColor = DabaiPrimary,
                contentColor = DabaiSurface,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = "拍照", modifier = Modifier.size(32.dp))
                    Text("拍照", fontSize = 10.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(nickname: String, personCount: Int) {
    TopAppBar(
        title = {
            Column {
                Text("大白伴家", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Text(
                    "守护 $personCount 位家人",
                    fontSize = 12.sp,
                    color = DabaiSubtext
                )
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Outlined.Notifications, "通知", tint = DabaiOnPrimary)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Outlined.Person, "个人中心", tint = DabaiOnPrimary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DabaiBackground
        )
    )
}

@Composable
private fun SeatIndicator(used: Int, total: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("家人席位", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DabaiOnSurface)
        Spacer(modifier = Modifier.width(12.dp))
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(if (index < used) DabaiPrimary else DabaiDivider)
            )
            if (index < total - 1) Spacer(modifier = Modifier.width(6.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        Text("$used/$total", fontSize = 12.sp, color = DabaiSubtext)
    }
}

@Composable
private fun PersonCard(
    person: PersonEntity,
    onClick: () -> Unit,
    onTakePhoto: () -> Unit
) {
    val healthColor = when {
        person.healthScore >= 75 -> HealthGood
        person.healthScore >= 55 -> HealthWarn
        else -> HealthBad
    }

    Surface(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = DabaiSurface,
        elevation = SurfaceDefaults.elevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 关系头像
            RelationshipAvatar(
                relationship = person.relationship,
                nickname = person.nickname,
                size = 64.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = person.nickname,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DabaiOnSurface
            )
            Text(
                text = person.relationship,
                fontSize = 12.sp,
                color = DabaiSubtext
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 健康评分
            HealthScoreBar(score = person.healthScore, color = healthColor)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "已检测 ${person.recordCount} 次",
                fontSize = 11.sp,
                color = DabaiSubtext
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onTakePhoto,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DabaiPrimary
                )
            ) {
                Icon(Icons.Filled.CameraAlt, "拍照", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("拍照检测", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun AddPersonCard(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = DabaiSurface,
        elevation = SurfaceDefaults.elevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(DabaiPrimaryLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Add,
                    "添加家人",
                    modifier = Modifier.size(32.dp),
                    tint = DabaiPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("添加家人", fontWeight = FontWeight.Medium, color = DabaiPrimary)
            Text("最多3位", fontSize = 12.sp, color = DabaiSubtext)
        }
    }
}
