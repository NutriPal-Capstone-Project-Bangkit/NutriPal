package com.example.nutripal.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutripal.R
import com.example.nutripal.ui.theme.Disabled

@Composable
fun AdsFrame() {
    Box(
        modifier = Modifier
            .width(320.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp)) // Using clip for rounded corners
            .background(Disabled) // Background color as placeholder for the frame
    ) {
        // Custom background shape (vector-like drawable)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Applying custom path/shape like in the vector drawable (Clip path)
                    shape = RoundedCornerShape(16.dp) // Apply rounded corner shape
                    clip = true
                }
        ) {
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center) // Center content inside
                .padding(16.dp) // Some padding
        ) {
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFrame2057() {
    AdsFrame()
}
