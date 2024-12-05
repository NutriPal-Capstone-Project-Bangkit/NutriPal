package com.example.nutripal.ui.component.home.ads

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutripal.ui.theme.Disabled

@Composable
fun AdsFrame(imagePainter: Painter) {
    Box(
        modifier = Modifier
            .width(320.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Disabled)
    ) {
        Image(
            painter = imagePainter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)), // Rounded corners for the image
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewFrame2057() {
//    AdsFrame(Painter)
//}
