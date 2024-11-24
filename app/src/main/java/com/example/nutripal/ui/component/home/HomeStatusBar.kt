package com.example.nutripal.ui.component.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.luminance
import com.example.nutripal.ui.theme.Primary
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun HomeStatusBar() {
    val systemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(
        color = Primary,
        darkIcons = Primary.luminance() > 0.5f
    )
}
