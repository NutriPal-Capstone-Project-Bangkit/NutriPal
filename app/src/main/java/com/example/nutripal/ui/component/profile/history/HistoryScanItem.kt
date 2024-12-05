package com.example.nutripal.ui.component.profile.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.nutripal.R
import com.example.nutripal.ui.theme.NunitoFontFamily

@Composable
fun HistoryScanItem(
    imageUrl: String,
    title: String,
    dateTime: String,
    carbs: String,
    protein: String,
    fat: String,
    modifier: Modifier
) {
    Row(
        modifier = Modifier
            .size(width = 365.dp, height = 100.dp) // Ukuran tetap 365x100 dp
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
    ) {
        // Image
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = "Item Image",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.fillMaxHeight().weight(1f)) {
            // Title
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            // Date and Time
            Text(
                text = dateTime,
                fontSize = 12.sp,
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Nutrition Info Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                NutritionInfo(icon = R.drawable.ic_carbs, value = carbs, color = Color(0xFF2196F2))
                Spacer(modifier = Modifier.width(12.dp))
                NutritionInfo(icon = R.drawable.ic_protein, value = protein, color = Color(0xFFF67724))
                Spacer(modifier = Modifier.width(12.dp))
                NutritionInfo(icon = R.drawable.ic_fat, value = fat, color = Color(0XFFF13030))
            }
        }
    }
}

@Composable
fun NutritionInfo(icon: Int, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = color
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHistoryScanItem() {
    HistoryScanItem(
        imageUrl = "https://via.placeholder.com/150",
        title = "Drink",
        dateTime = "2024-12-18 | 20.20",
        carbs = "18g",
        protein = "3g",
        fat = "4g",
        modifier = Modifier
    )
}
