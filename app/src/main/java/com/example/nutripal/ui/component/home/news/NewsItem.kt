package com.example.nutripal.ui.component.home.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.darkGray

data class NewsItem(
    val id: String,
    val imageUrl: String,
    val title: String,
    val subtitle: String,
    val url: String,
)

@Composable
fun NewsItemCard(
    news: NewsItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.12f))
            ) {
                Image(
                    painter = rememberAsyncImagePainter(news.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(9f / 4f),
                    contentScale = ContentScale.Crop
                )
            }

            // Text content section with white background
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = NunitoFontFamily,
                        fontSize = 18.sp
                    ),
                    fontWeight = FontWeight.Bold,
                    color = darkGray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = news.subtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = NunitoFontFamily,
                        fontSize = 14.sp
                    ),
                    color = darkGray
                )

            }
        }
    }
}
