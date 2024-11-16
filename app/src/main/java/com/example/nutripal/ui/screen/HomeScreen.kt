package com.example.nutripal.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import androidx.compose.ui.zIndex
import com.example.nutripal.R
import com.example.nutripal.ui.component.AdsFrame
import com.example.nutripal.ui.component.BottomNavigationBar
import com.example.nutripal.ui.component.NewsItem
import com.example.nutripal.ui.component.NewsItemCard
import com.example.nutripal.ui.theme.Disabled
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.ui.theme.darkGray

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
                .background(Primary)
                .zIndex(0f)
                .verticalScroll(rememberScrollState()) // Tambahkan scroll di sini
        )

        // Konten utama
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
                .verticalScroll(rememberScrollState())
                .zIndex(1f)
        ) {
            // Header di bagian atas
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Hola 👋",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Yuk, cek asupan harianmu!",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Daily Nutrition Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Daily Nutritions",
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    NutrientProgress(
                        label = "Karbohidrat",
                        progress = 0.8f,
                        color = Color(0xFF2196F2),
                        value = "800 g",
                        iconResId = R.drawable.ic_carbs
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    NutrientProgress(
                        label = "Protein",
                        progress = 0.5f,
                        color = Color(0xFFF67724),
                        value = "500 g",
                        iconResId = R.drawable.ic_protein
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    NutrientProgress(
                        label = "Lemak",
                        progress = 0.5f,
                        color = Color(0xFFF13030),
                        value = "500 g",
                        iconResId = R.drawable.ic_fat
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        modifier = Modifier
                            .align(Alignment.End)
                            .border(0.5.dp, darkGray, RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Transparent,
                    ) {
                        Text(
                            text = "Today, 8 Nov 2024",
                            color = darkGray,
                            fontSize = 12.sp,
                            fontFamily = NunitoFontFamily,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Konten lainnya
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color.Transparent)
                    .padding(horizontal = 16.dp)
            ) {
                Button(
                    onClick = { /* TODO */ },
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
                LazyRow(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    items(5) {
                        AdsFrame()
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
                Text(
                    text = "Baca Artikel Terbaru",
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Text(
                    text = "Temukan inspirasi dan pengetahuan untuk hidup sehat.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            val sampleNews = NewsItem(
                imageUrl = "https://example.com/news-image.jpg",
                title = "Breaking News: Major Scientific Discovery",
                subtitle = "Lorem ipsum"
            )
            for(i in 1..5)run {
            NewsItemCard(
                news = sampleNews,
                modifier = Modifier.padding(16.dp)
            )
        }
        }

        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 15.dp)
                .zIndex(2f)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp) // Ukuran area yang bisa di-click lebih kecil
                    .align(Alignment.Center)
                    .clickable(onClick = { /* TODO: Handle scanner click */ })
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_scanner),
                contentDescription = "Scanner",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(150.dp) // Ukuran visual icon tetap sama
                    .align(Alignment.Center)
            )
        }


        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .zIndex(1f),
            color = Color.White,
            shadowElevation = 8.dp,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp, top = 16.dp)
            ) {
                BottomNavigationBar(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun NutrientProgress(
    label: String,
    progress: Float,
    color: Color,
    value: String,
    iconResId: Int
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
                    modifier = Modifier.size(30.dp),
                    tint = color
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
                progress = progress,
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = color,
                trackColor = Color.LightGray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
