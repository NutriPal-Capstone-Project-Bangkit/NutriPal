package com.example.nutripal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.nutripal.R

@Composable
fun AuthHeaderImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.nutripal_login_word),
        contentDescription = "NutriPal Logo",
        modifier = modifier
            .width(100.dp)
            .height(100.dp)
    )
}
