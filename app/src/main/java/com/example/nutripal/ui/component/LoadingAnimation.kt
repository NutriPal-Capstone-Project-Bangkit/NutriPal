package com.example.nutripal.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutripal.ui.theme.Primary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    dotSize: Float = 20f,
    dotSpacing: Float = 80f,
    animationDuration: Int = 2000
) {
    val infiniteTransition = rememberInfiniteTransition()

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = LinearEasing
            )
        )
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration / 2),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .size((dotSpacing * 2).dp)
    ) {
        val radius = dotSpacing / 2

        val positions = listOf(
            calculateDotPosition(0f + rotation, radius),
            calculateDotPosition(90f + rotation, radius),
            calculateDotPosition(180f + rotation, radius),
            calculateDotPosition(270f + rotation, radius)
        )

        positions.forEachIndexed { index, position ->
            Dot(
                color = if (index % 2 == 0) Primary else Color(0xFFCC5252),
                size = dotSize * scale,
                x = position.first,
                y = position.second
            )
        }
    }
}

private fun calculateDotPosition(
    angle: Float,
    radius: Float
): Pair<Float, Float> {
    val rad = Math.toRadians(angle.toDouble())
    return Pair(
        (radius * (1 + cos(rad))).toFloat(),
        (radius * (1 + sin(rad))).toFloat()
    )
}

@Composable
private fun Dot(
    color: Color,
    size: Float,
    x: Float,
    y: Float
) {
    Box(
        modifier = Modifier
            .offset(
                x = with(LocalDensity.current) { x.toDp() },
                y = with(LocalDensity.current) { y.toDp() }
            )
            .size(size.dp)
            .background(color = color, shape = CircleShape)
    )
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingAnimation()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoadingScreen() {
    LoadingScreen()
}