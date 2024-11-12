package com.example.nutripal.ui.custom

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.ui.theme.Secondary

@Composable
fun CustomCanvas(
    modifier: Modifier = Modifier,
    secondaryColor: Color = Secondary,
    primaryColor: Color = Primary
) {
    Canvas(
        modifier = modifier
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        rotate(degrees = 22.0f) {
            drawRoundRect(
                color = secondaryColor,
                size = Size(
                    width = canvasWidth * 2.0f,
                    height = canvasHeight * 5.0f
                ),
                cornerRadius = CornerRadius(
                    40.dp.toPx(),
                    40.dp.toPx()
                ),
                topLeft = Offset(x = canvasWidth * 0.088f, y = canvasHeight * 0.05f)
            )

            drawRoundRect(
                color = primaryColor,
                size = Size(
                    width = canvasWidth * 2.0f,
                    height = canvasHeight * 5.0f
                ),
                cornerRadius = CornerRadius(
                    40.dp.toPx(),
                    40.dp.toPx()
                ),
                topLeft = Offset(x = canvasWidth * 0.20f, y = canvasHeight * -0.11f)
            )
        }
    }
}
