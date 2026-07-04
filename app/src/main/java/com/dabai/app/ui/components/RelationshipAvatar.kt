package com.dabai.app.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dabai.app.ui.theme.*
@Composable
fun RelationshipAvatar(relationship: String, nickname: String, size: Dp = 48.dp, fontSize: TextUnit = 20.sp) {
    val bgColor = when (relationship) {
        "爸爸", "父亲" -> RelationshipDad
        "妈妈", "母亲" -> RelationshipMom
        "爷爷", "外公" -> RelationshipGrandpa
        "奶奶", "外婆" -> RelationshipGrandma
        "哥哥", "弟弟" -> RelationshipBrother
        "姐姐", "妹妹" -> RelationshipSister
        else -> RelationshipDefault
    }
    val displayChar = nickname.take(1)
    Box(modifier = Modifier.size(size).clip(CircleShape).background(bgColor), contentAlignment = Alignment.Center) {
        Text(text = displayChar, fontSize = fontSize, fontWeight = FontWeight.Bold, color = Color.White)
    }
}
