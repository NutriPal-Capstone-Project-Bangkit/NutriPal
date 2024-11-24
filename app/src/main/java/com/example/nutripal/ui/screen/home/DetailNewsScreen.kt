package com.example.nutripal.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.nutripal.R
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.util.formatDate
import com.example.nutripal.viewmodel.DetailNewsViewModel
import com.example.nutripal.ui.component.home.ads.detail.ReadMoreButton
import android.text.Html

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    navController: NavController,
    viewModel: DetailNewsViewModel = hiltViewModel()
) {
    val newsDetail by viewModel.newsDetail.collectAsState()
    val scrollState = rememberScrollState()

    MainStatusBar()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Artikel",
                        color = Primary,
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // Header Image
            newsDetail?.urlToImage?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Source
                newsDetail?.source?.name?.let { sourceName ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_author),
                            contentDescription = "Source Icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = sourceName,
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        )
                    }
                }

                // Title
                Text(
                    text = newsDetail?.title.orEmpty(),
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Date
                newsDetail?.publishedAt?.let { date ->
                    Text(
                        text = formatDate(date),
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                newsDetail?.description?.let { description ->
                    Text(
                        text = description,
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Content with HTML Formatting
                newsDetail?.content?.let { content ->
                    Text(
                        text = Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY).toString(),
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Read More Button
                ReadMoreButton(newsDetail = newsDetail)
            }
        }
    }
}
