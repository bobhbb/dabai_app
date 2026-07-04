package com.dabai.app.ui.screens.splash

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dabai.app.ui.theme.DabaiPrimary
import com.dabai.app.ui.theme.DabaiSurface
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    var showContent by remember { mutableStateOf(false) }
    var alpha by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        showContent = true
        delay(500)
        alpha = 1f
        delay(1500)
        // 检查登录状态（演示直接跳登录）
        onNavigateToLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DabaiPrimary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(alpha)
        ) {
            // 大白机器人图标（文字示意，实际用图标/动画）
            Text(
                text = "🤖",
                fontSize = 80.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "大白",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = DabaiSurface,
                letterSpacing = 8.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "家庭健康陪伴",
                fontSize = 18.sp,
                color = DabaiSurface.copy(alpha = 0.8f),
                fontWeight = FontWeight.Light,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "用AI守护家人健康",
                fontSize = 14.sp,
                color = DabaiSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}
