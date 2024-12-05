package com.example.nutripal.ui.component.home.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutripal.util.chatbotFormatText

@Composable
fun BotChat(
    text: String,
    timestamp: String,
    backgroundColor: Color,
    textColor: Color
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(backgroundColor, RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .wrapContentWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = chatbotFormatText(text),
                    color = textColor,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
            Text(
                text = timestamp,
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}