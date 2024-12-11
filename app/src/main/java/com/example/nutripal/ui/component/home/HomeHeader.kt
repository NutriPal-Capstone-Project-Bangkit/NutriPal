package com.example.nutripal.ui.component.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.ui.component.LoadingAnimation
import com.example.nutripal.ui.component.home.ads.AdsFrame
import com.example.nutripal.ui.component.home.news.NewsItem
import com.example.nutripal.ui.component.home.news.NewsItemCard
import com.example.nutripal.ui.navigation.Screen
import com.example.nutripal.ui.screen.home.dailytrack.DailyNutritionTrackViewModel
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.ui.theme.darkGray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeHeader() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Hola 👋",
            color = Color.White,
            fontSize = 16.sp,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Yuk, cek asupan harianmu!",
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun DailyNutritionCard(
    navController: NavController,
    viewModel: DailyNutritionTrackViewModel = viewModel()
) {
    val nutritionItems by viewModel.nutritionItems.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()

    // Recommended daily intake values
    val recommendedCarbohydrate = 300 // g
    val recommendedProtein = 200 // g
    val recommendedFat = 150 // g

    // Calculate total nutrients for the day
    val totalCarbohydrate = nutritionItems.sumOf { it.carbohydrate }
    val totalProtein = nutritionItems.sumOf { it.protein }
    val totalFat = nutritionItems.sumOf { it.fat }

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
                    text = "Daily Nutritions",
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_continue),
                    contentDescription = "Continue Icon",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            navController.navigate("daily_track")
                        }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Carbohydrate Progress
            NutrientProgress(
                label = "Karbohidrat",
                progress = (totalCarbohydrate.toFloat() / recommendedCarbohydrate).coerceIn(0f, 1f),
                color = Color(0xFF2196F2),
                value = "$totalCarbohydrate g",
                iconResId = R.drawable.ic_carbs
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Protein Progress
            NutrientProgress(
                label = "Protein",
                progress = (totalProtein.toFloat() / recommendedProtein).coerceIn(0f, 1f),
                color = Color(0xFFF67724),
                value = "$totalProtein g",
                iconResId = R.drawable.ic_protein
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Fat Progress
            NutrientProgress(
                label = "Lemak",
                progress = (totalFat.toFloat() / recommendedFat).coerceIn(0f, 1f),
                color = Color(0xFFF13030),
                value = "$totalFat g",
                iconResId = R.drawable.ic_fat
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Date Display
            DisplayDate(selectedDate = selectedDate)
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
            text = "Today, $formattedDate",
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = darkGray
        )
    }
}

@Composable
fun HomeContent(
    navController: NavController,
    newsList: List<NewsItem>,
    isLoading: Boolean,
    errorMessage: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color.Transparent)
            .padding(horizontal = 16.dp)
    ) {
        NutriAIButton(navController)
        AdsSection()
        ArticleSection()


        NewsSection(
            navController = navController,
            newsList = newsList,
            isLoading = isLoading,
            errorMessage = errorMessage
        )
    }
}

@Composable
fun DateDisplay() {
    val currentDate = SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(Date())
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.CenterEnd)
            .border(0.5.dp, darkGray, RoundedCornerShape(16.dp)) // Border dengan rounded corner sesuai permintaan
            .padding(horizontal = 12.dp, vertical = 6.dp) // Padding untuk teks
    ) {
        Text(
            text = "Today, $currentDate",
            color = darkGray,
            fontSize = 12.sp,
            fontFamily = NunitoFontFamily,
            modifier = Modifier.align(Alignment.CenterEnd) // Teks ter-align ke kanan
        )
    }
}

@Composable
fun NutriAIButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(Screen.Chatbot.route)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF5722)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_go_to_chatbot),
                contentDescription = "Go to bot",
                tint = Color.White,
                modifier = Modifier
                    .size(25.dp)
                    .padding(end = 12.dp)
            )
            Text(
                text = "Nutri AI",
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun AdsSection() {
    val ads = listOf(
        R.drawable.ads1,
        R.drawable.ads2,
        R.drawable.ads3
    )

    LazyRow(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        items(ads) { ad ->
            AdsFrame(imagePainter = painterResource(id = ad))
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
fun ArticleSection() {
    Column {
        Text(
            text = "Baca Artikel Terbaru",
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Text(
            text = "Temukan inspirasi dan pengetahuan untuk hidup sehat.",
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun NewsSection(
    newsList: List<NewsItem>,
    isLoading: Boolean,
    errorMessage: String?,
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LoadingAnimation(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = (50).dp)
                )
            }
        } else if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp
            )
        } else {
            Column {
                newsList.take(5).forEach { news ->
                    NewsItemCard(
                        news = news,
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            try {
                                val route = Screen.NewsDetail.createRoute(news.url)
                                navController.navigate(route)
                            } catch (e: Exception) {
                                Log.e("Navigation", "Error navigating to news detail", e)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){
    DailyNutritionCard(navController = rememberNavController())
}