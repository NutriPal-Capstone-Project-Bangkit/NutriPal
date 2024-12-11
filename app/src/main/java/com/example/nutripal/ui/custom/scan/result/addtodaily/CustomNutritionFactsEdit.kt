package com.example.nutripal.ui.custom.scan.result.addtodaily

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutripal.R
import com.example.nutripal.data.model.NutritionFacts
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.ui.theme.darkGray

@Composable
fun CustomNutritionFactsEdit(
    nutritionFacts: NutritionFacts,
    onCarbohydrateChange: (Int) -> Unit,
    onProteinChange: (Int) -> Unit,
    onFatChange: (Int) -> Unit
) {
    var carbohydrateValue by remember { mutableStateOf("${nutritionFacts.carbohydrate}") }
    var proteinValue by remember { mutableStateOf("${nutritionFacts.protein}") }
    var fatValue by remember { mutableStateOf("${nutritionFacts.fat}") }


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
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Berikut hasil kandungan gizi makananmu!",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Nutrient details with icons
            FactRow(
                label = "Karbohidrat",
                value = carbohydrateValue,
                iconResId = R.drawable.ic_carbs,
                onValueChange = { value ->
                    carbohydrateValue = value
                    value.toIntOrNull()?.let { onCarbohydrateChange(it) }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            FactRow(
                label = "Protein",
                value = proteinValue,
                iconResId = R.drawable.ic_protein,
                onValueChange = { value ->
                    proteinValue = value
                    value.toIntOrNull()?.let { onProteinChange(it) }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            FactRow(
                label = "Lemak",
                value = fatValue,
                iconResId = R.drawable.ic_fat,
                onValueChange = { value ->
                    fatValue = value
                    value.toIntOrNull()?.let { onFatChange(it) }
                }
            )
        }
    }
}

@Composable
fun FactRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    iconResId: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
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
                color = Color.Black
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            BasicTextField(
                value = value,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) {
                        onValueChange(input)
                    }
                },
                modifier = Modifier
                    .width(35.dp)
                    .height(24.dp)
                    .border(0.1f.dp, Color.DarkGray, RoundedCornerShape(4.dp))
                    .padding(top = 2.dp),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        innerTextField()
                    }
                }
            )
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(color = Primary, shape = RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "gr",
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    CustomNutritionFactsEdit(
        nutritionFacts = NutritionFacts(
            carbohydrate = 25,
            protein = 5,
            fat = 10
        ),
        onCarbohydrateChange = {},
        onFatChange = {},
        onProteinChange = {}
    )
}
