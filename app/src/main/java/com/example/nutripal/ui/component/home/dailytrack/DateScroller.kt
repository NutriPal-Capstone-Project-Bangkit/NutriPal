package com.example.nutripal.ui.component.home.dailytrack

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle // Use this with a qualified name if needed
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Secondary
import java.time.LocalDate
import java.util.Locale
import java.time.format.TextStyle as JavaTextStyle // Alias for the `TextStyle` class from `java.time.format`

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DateScroller(
    selectedDate: LocalDate,
    updateSelectedDate: (LocalDate) -> Unit,
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val dates = (1..30).map { day -> selectedDate.withDayOfMonth(day) }

        items(dates.size) { index ->
            val date = dates[index]
            val isSelected = date == selectedDate

            Card(
                modifier = Modifier
                    .width(52.dp)  // Lebar sesuai spesifikasi
                    .height(62.dp) // Tinggi sesuai spesifikasi
                    .padding(vertical = 8.dp)
                    .clickable { updateSelectedDate(date) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Secondary else Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (isSelected) 8.dp else 4.dp
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val monthName = date.month.getDisplayName(JavaTextStyle.SHORT, Locale.getDefault())

                    Text(
                        text = monthName,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDateScroller() {
    val currentDate = LocalDate.now()
    DateScroller(
        selectedDate = currentDate,
        updateSelectedDate = { newDate ->  }
    )
}
