package com.example.nutripal.ui.component.result

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.nutripal.R
import com.example.nutripal.data.model.NutritionFacts
import com.example.nutripal.ui.theme.Primary

@Composable
fun NutritionFactsView(nutritionFacts: NutritionFacts) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title and subtitle
            Text(
                text = "Nutrition Facts",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Berikut hasil kandungan gizi makananmu!",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Nutrient details with icons
            FactRow(
                label = "Karbohidrat",
                value = "${nutritionFacts.carbohydrate} gram",
                color = Color(0xFF2196F2), // Blue
                iconResId = R.drawable.ic_carbs // Replace with actual resource ID
            )
            Spacer(modifier = Modifier.height(8.dp))
            FactRow(
                label = "Protein",
                value = "${nutritionFacts.protein} gram",
                color = Color(0xFFF67724),
                iconResId = R.drawable.ic_protein
            )
            Spacer(modifier = Modifier.height(8.dp))
            FactRow(
                label = "Lemak",
                value = "${nutritionFacts.fat} gram",
                color = Color(0xFFF13030),
                iconResId = R.drawable.ic_fat
            )
        }
    }
}

@Composable
fun FactRow(label: String, value: String, color: Color, iconResId: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = "$label icon",
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = Primary
        )
    }
}
