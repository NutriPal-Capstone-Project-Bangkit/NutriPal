package com.example.nutripal.ui.component.result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.example.nutripal.data.model.NutritionFacts
import com.example.nutripal.ui.theme.NunitoFontFamily
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
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                fontFamily = NunitoFontFamily,
                text = "Berikut hasil kandungan gizi makananmu!",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Nutrient details with icons
            FactRow(
                label = "Karbohidrat",
                value = "${nutritionFacts.carbohydrate} gram",
                iconResId = R.drawable.ic_carbs
            )
            Spacer(modifier = Modifier.height(8.dp))
            FactRow(
                label = "Protein",
                value = "${nutritionFacts.protein} gram",
                iconResId = R.drawable.ic_protein
            )
            Spacer(modifier = Modifier.height(8.dp))
            FactRow(
                label = "Lemak",
                value = "${nutritionFacts.fat} gram",
                iconResId = R.drawable.ic_fat
            )
        }
    }
}

@Composable
fun FactRow(label: String, value: String, iconResId: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = "$label icon",
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            fontFamily = NunitoFontFamily,
            text = value,
            fontSize = 16.sp,
            color = Primary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview1(){
    NutritionFactsView(NutritionFacts(10,20,30))
}
