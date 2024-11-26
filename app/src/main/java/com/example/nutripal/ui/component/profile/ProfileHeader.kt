package com.example.nutripal.ui.component.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutripal.ui.theme.NunitoFontFamily

@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Atur",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 16.dp),
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Pengaturan Profilemu",
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.SemiBold
        )
    }
}