package com.example.nutripal.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary

@Composable
fun PersonalDetailsHeader(currentPage: Int, pageCount: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Pribadimu",
            color = Primary,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = NunitoFontFamily,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Page Indicator
        PageIndicator(currentPage = currentPage, pageCount = pageCount)
    }
}
