package com.example.nutripal.ui.component.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutripal.R
import com.example.nutripal.ui.component.home.dailytrack.DailyNutritionTrackCard
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.darkGray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NutrientProgress(
    label: String,
    progress: Float,
    color: Color,
    value: String,
    iconResId: Int,
    iconModifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .offset(y = 12.dp),
                    tint = Color.Unspecified
                )
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontFamily = NunitoFontFamily
                )
            }
            Text(
                text = value,
                fontSize = 14.sp,
                fontFamily = NunitoFontFamily,
                modifier = Modifier.padding(start = 8.dp),
                color = darkGray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.width(32.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = color,
                trackColor = Color.LightGray,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(){
    NutrientProgress(
        label = "Lemak",
        progress = 0.5f,
        color = Color(0xFFF13030),
        value = "500 g",
        iconResId = R.drawable.ic_fat,
        iconModifier = Modifier.offset(y = 8.dp)
    )
}