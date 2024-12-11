package com.example.nutripal.ui.component.home.dailytrack

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutripal.R
import com.example.nutripal.ui.component.home.NutrientProgress
import com.example.nutripal.ui.screen.home.dailytrack.DailyNutritionTrackViewModel
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.darkGray
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DailyNutritionTrackCard(
    viewModel: DailyNutritionTrackViewModel = viewModel()
) {
    val nutritionItems by viewModel.nutritionItems.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()

    val totalCarbohydrate = nutritionItems.sumOf { it.carbohydrate }
    val totalProtein = nutritionItems.sumOf { it.protein }
    val totalFat = nutritionItems.sumOf { it.fat }

    val recommendedCarbohydrate = 300 // g
    val recommendedProtein = 200 // g
    val recommendedFat = 150 // g

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Persentase Asupan",
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                DisplayDate(selectedDate = selectedDate)
            }
            Spacer(modifier = Modifier.height(16.dp))
            NutrientProgress(
                label = "Karbohidrat",
                progress = (totalCarbohydrate.toFloat() / recommendedCarbohydrate).coerceIn(0f, 1f),
                color = Color(0xFF2196F2),
                value = "$totalCarbohydrate g",
                iconResId = R.drawable.ic_carbs,
                iconModifier = Modifier.offset(y = 4.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            NutrientProgress(
                label = "Protein",
                progress = (totalProtein.toFloat() / recommendedProtein).coerceIn(0f, 1f),
                color = Color(0xFFF67724),
                value = "$totalProtein g",
                iconResId = R.drawable.ic_protein,
                iconModifier = Modifier.offset(y = 4.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            NutrientProgress(
                label = "Lemak",
                progress = (totalFat.toFloat() / recommendedFat).coerceIn(0f, 1f),
                color = Color(0xFFF13030),
                value = "$totalFat g",
                iconResId = R.drawable.ic_fat,
                iconModifier = Modifier.offset(y = 4.dp)
            )
        }
    }
}

@Composable
fun DisplayDate(selectedDate: LocalDate) {
    val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault()))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.CenterEnd)
            .border(0.5.dp, darkGray, RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Text(
            text = formattedDate,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = darkGray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(){
    DailyNutritionTrackCard()
}
