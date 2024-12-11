package com.example.nutripal.ui.component.home.dailytrack.addtodaily

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutripal.R
import com.example.nutripal.ui.theme.NunitoFontFamily

@Composable
fun AddToDailyRecommendation(recommendation: String, isRefreshEnabled: Boolean, onRefreshClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Title and subtitle
                Text(
                    text = "Rekomendasi",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = NunitoFontFamily,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    fontFamily = NunitoFontFamily,
                    text = "Berikut saran untuk konsumsi makananmu:",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Displaying the recommendation text
                Text(
                    fontFamily = NunitoFontFamily,
                    text = recommendation,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Refresh icon (conditionally visible)
            if (isRefreshEnabled) {
                IconButton(
                    onClick = onRefreshClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_refresh),
                        modifier = Modifier.size(20.dp),
                        contentDescription = "Refresh",
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEnabledRefresh() {
    AddToDailyRecommendation("Hai", isRefreshEnabled = true)
}

@Preview(showBackground = true)
@Composable
fun PreviewDisabledRefresh() {
    AddToDailyRecommendation("Hai", isRefreshEnabled = false)
}
