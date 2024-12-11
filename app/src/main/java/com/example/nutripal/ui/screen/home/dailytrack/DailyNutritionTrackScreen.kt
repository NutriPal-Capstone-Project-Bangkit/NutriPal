package com.example.nutripal.ui.screen.home.dailytrack

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.home.dailytrack.DailyNutritionTrackCard
import com.example.nutripal.ui.component.profile.history.HistoryScanItem
import com.example.nutripal.ui.navigation.Screen
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.ui.theme.Secondary

@Composable
fun DailyNutritionTrackScreen(
    navController: NavController,
    viewModel: DailyNutritionTrackViewModel = viewModel()
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val nutritionItems by viewModel.nutritionItems.collectAsState()

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = selectedDate.dayOfMonth - 1
    )

    MainStatusBar()

    Scaffold(
        topBar = {
            DailyNutritionTopBar(
                navController = navController,
                onCalendarClick = { /* Handle calendar click */ }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
            ) {
                // Kalender LazyRow
                LazyRow(
                    state = listState,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) {
                    // Generate semua tanggal dalam sebulan
                    val dates = (1..30)
                        .map { day -> selectedDate.withDayOfMonth(day) }

                    items(dates) { date ->
                        val isSelected = date == selectedDate
                        Card(
                            modifier = Modifier
                                .width(50.dp)
                                .padding(vertical = 8.dp)
                                .clickable { viewModel.updateSelectedDate(date) },
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
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = viewModel.getMonthName(date),
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

                Spacer(modifier = Modifier.height(16.dp))

                DailyNutritionTrackCard()

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Jejak Konsumsi Harianmu",
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF333333)
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Lihat asupan harianmu dan pantau kemajuan menuju tujuan sehat!",
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Color(0xFF333333)
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Nutrition Items LazyColumn
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                ) {
                    items(nutritionItems) { item ->
                        HistoryScanItem(
                            imageUrl = item.image,
                            title = item.product,
                            dateTime = item.timestamp,
                            carbs = "${item.carbohydrate}g",
                            protein = "${item.protein}g",
                            fat = "${item.fat}g",
                            modifier = Modifier,
                            onClick = {navController.navigate("daily_track_details/${item.id}")}
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DailyNutritionTopBar(
    navController: NavController,
    onCalendarClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Nutrisi Harian",
                color = Primary,
                style = TextStyle(
                    fontFamily = NunitoFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
        },
        actions = {
            IconButton(onClick = onCalendarClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "Calendar",
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        modifier = Modifier.background(
            MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    DailyNutritionTrackScreen(navController = rememberNavController())
}