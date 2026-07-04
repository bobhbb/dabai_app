package com.dabai.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dabai.app.ui.theme.*

@Composable
fun HealthScoreBar(score: Float, color: Color = when { score >= 75 -> HealthGood, score >= 55 -> HealthWarn, else -> HealthBad }, showText: Boolean = true) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (showText) {
            Text(text = "${score.toInt()}分", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(4.dp))
        }
        Box(modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)).background(DabaiDivider)) {
            Box(modifier = Modifier.fillMaxWidth(score / 100f).fillMaxHeight().clip(RoundedCornerShape(3.dp)).background(color))
        }
    }
}

@Composable
fun HealthScoreIndicator(score: Float, modifier: Modifier = Modifier) {
    val (color, label) = when { score >= 75 -> HealthGood to "良好", score >= 55 -> HealthWarn to "一般", else -> HealthBad to "需关注" }
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(color))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, fontSize = 12.sp, color = color, fontWeight = FontWeight.Medium)
    }
}
