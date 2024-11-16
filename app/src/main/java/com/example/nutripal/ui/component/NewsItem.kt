package com.example.nutripal.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import coil.compose.rememberAsyncImagePainter
import com.example.nutripal.ui.theme.NunitoFontFamily

data class NewsItem(
    val imageUrl: String,
    val title: String,
    val subtitle: String
)

@Composable
fun NewsItemCard(
    news: NewsItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column {
            // Image section with disabled color background
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.12f))
            ) {
                Image(
                    painter = rememberAsyncImagePainter(news.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(9f/4f),
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
                    style = MaterialTheme.typography.titleLarge.copy(fontFamily = NunitoFontFamily),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = news.subtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = NunitoFontFamily),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsItemPreview() {
    val sampleNews = NewsItem(
        imageUrl = "https://example.com/news-image.jpg",
        title = "Breaking News: Major Scientific Discovery",
        subtitle = "Lorem ipsum dolor sit amet"
    )

    NewsItemCard(
        news = sampleNews,
        modifier = Modifier.padding(16.dp)
    )
}