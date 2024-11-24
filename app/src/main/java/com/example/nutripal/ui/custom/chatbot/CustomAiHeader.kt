package com.example.nutripal.ui.custom.chatbot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.nutripal.R

@Composable
fun CustomAiHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize() // Makes container size wrap the image size
    ) {
        // Display Image for AI Header
        Image(
            painter = painterResource(id = R.drawable.ic_nutriai),
            contentDescription = "NutriPal AI Logo",
            modifier = Modifier
                .size(300.dp) // Adjust the size to make the image larger (e.g., 150.dp)
                .clip(CircleShape)
                .background(Color.Transparent) // Transparent background
        )
    }
}

