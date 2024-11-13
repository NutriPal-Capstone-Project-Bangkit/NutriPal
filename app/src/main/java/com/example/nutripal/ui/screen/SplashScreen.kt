@file:Suppress("FunctionName")

package com.example.nutripal.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Suppress("FunctionName")
@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var alpha by remember { mutableFloatStateOf(0f) }

    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 1000), label = ""
    )

    LaunchedEffect(Unit) {
        delay(500)
        alpha = 1f
        splashViewModel.navigateToOnboarding(navController)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Primary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_text),
            contentDescription = "Splash Text",
            modifier = Modifier.alpha(animatedAlpha)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}
