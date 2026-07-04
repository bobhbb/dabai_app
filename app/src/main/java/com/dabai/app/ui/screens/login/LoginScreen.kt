package com.dabai.app.ui.screens.login

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dabai.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    var isLoggingIn by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DabaiBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 大白形象
            Text(
                text = "🤖",
                fontSize = 100.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "大白伴家",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = DabaiOnPrimary,
                letterSpacing = 6.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "用AI守护家人健康",
                fontSize = 16.sp,
                color = DabaiSubtext,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 功能介绍
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DabaiSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    FeatureItem("📸", "拍照即健康分析", "拍张照片，AI自动分析家人健康状态")
                    Spacer(modifier = Modifier.height(12.dp))
                    FeatureItem("📊", "持续健康追踪", "每次拍照对比上次，看改善效果")
                    Spacer(modifier = Modifier.height(12.dp))
                    FeatureItem("✅", "个性化行动计划", "每日/周/月/年待办清单，轻松打卡")
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // 微信登录按钮
            val scope = rememberCoroutineScope()
            Button(
                onClick = {
                    isLoggingIn = true
                    scope.launch {
                        delay(1500)
                        onLoginSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DabaiPrimary,
                    contentColor = DabaiSurface
                ),
                enabled = !isLoggingIn
            ) {
                if (isLoggingIn) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = DabaiSurface,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Text(
                    text = if (isLoggingIn) "微信授权中..." else "微信登录",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 用户协议
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "登录即表示同意 ",
                    fontSize = 12.sp,
                    color = DabaiSubtext
                )
                Text(
                    text = "用户协议",
                    fontSize = 12.sp,
                    color = DabaiPrimary,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { }
                )
                Text(
                    text = " 和 ",
                    fontSize = 12.sp,
                    color = DabaiSubtext
                )
                Text(
                    text = "隐私政策",
                    fontSize = 12.sp,
                    color = DabaiPrimary,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { }
                )
            }
        }
    }
}

@Composable
private fun FeatureItem(icon: String, title: String, desc: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = icon, fontSize = 28.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = DabaiOnSurface
            )
            Text(
                text = desc,
                fontSize = 12.sp,
                color = DabaiSubtext
            )
        }
    }
}
