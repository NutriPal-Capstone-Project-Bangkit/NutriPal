package com.example.nutripal.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = NunitoFontFamily,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )
        // Page Indicator
        PersonalDetailsPageIndicator(currentPage = currentPage, pageCount = pageCount)
    }
}

@Composable
fun PersonalDetailsPageIndicator(
    currentPage: Int,
    pageCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(110.dp, 11.dp)
                    .background(
                        if (index <= currentPage) Primary else Color(0xFFB2D3B2),
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}
