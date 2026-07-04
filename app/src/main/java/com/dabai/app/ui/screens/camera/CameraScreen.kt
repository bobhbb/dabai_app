package com.dabai.app.ui.screens.camera

import android.Manifest
import android.graphics.BitmapFactory
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.viewinterop.AndroidView
import com.dabai.app.ui.theme.*
import com.google.accompanist.permissions.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    mode: String,
    onNewPerson: (faceToken: String, photoPath: String) -> Unit,
    onKnownPerson: (personId: String) -> Unit,
    onBack: () -> Unit
) {
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    var isAnalyzing by remember { mutableStateOf(false) }
    var showGuideline by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (!cameraPermission.status.isGranted) {
            cameraPermission.launchPermissionRequest()
        }
    }

    if (!cameraPermission.status.isGranted) {
        CameraPermissionRequest(onGranted = { cameraPermission.launchPermissionRequest() })
    } else {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            // 相机预览
            AndroidView(
                factory = { context ->
                    PreviewView(context).apply { implementationMode = PreviewView.ImplementationMode.COMPATIBLE }
                },
                modifier = Modifier.fillMaxSize()
            )

            // 人脸引导框
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                if (showGuideline) {
                    Surface(
                        modifier = Modifier
                            .size(280.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Transparent
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "将人脸对准框内\n保持光线充足",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // 遮罩层说明
            Column(modifier = Modifier.fillMaxSize()) {
                // 顶部关闭按钮
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.Close, "关闭", tint = Color.White)
                    }
                    Text(
                        text = "拍摄家人照片",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    IconButton(onClick = { showGuideline = !showGuideline }) {
                        Icon(Icons.Outlined.HelpOutline, "提示", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // 底部按钮
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 48.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isAnalyzing) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = Color.White, strokeWidth = 3.dp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("AI 分析中...", color = Color.White, fontSize = 14.sp)
                        }
                    } else {
                        FloatingActionButton(
                            onClick = {
                                isAnalyzing = true
                                scope.launch {
                                    delay(2000)
                                    // 模拟: 检测到新人物
                                    if (mode == "new" || System.currentTimeMillis() % 2 == 0L) {
                                        onNewPerson("face_token_demo", "/path/to/photo.jpg")
                                    } else {
                                        onKnownPerson("person_id_demo")
                                    }
                                }
                            },
                            modifier = Modifier.size(80.dp),
                            containerColor = Color.White,
                            contentColor = DabaiPrimary,
                            shape = CircleShape,
                            elevation = FloatingActionButtonDefaults.elevation(8.dp)
                        ) {
                            Icon(Icons.Filled.CameraAlt, "拍照", modifier = Modifier.size(40.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CameraPermissionRequest(onGranted: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(DabaiBackground), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Icon(Icons.Filled.PhotoCamera, "相机", modifier = Modifier.size(64.dp), tint = DabaiPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text("大白需要相机权限", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DabaiOnSurface)
            Spacer(modifier = Modifier.height(8.dp))
            Text("用于拍摄家人照片进行健康分析", fontSize = 14.sp, color = DabaiSubtext, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onGranted, modifier = Modifier.fillMaxWidth(0.7f), shape = RoundedCornerShape(24.dp)) {
                Text("授予权限")
            }
        }
    }
}
