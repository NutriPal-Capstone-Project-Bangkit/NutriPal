package com.example.nutripal.ui.screen.profile.history.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.nutripal.R
import com.example.nutripal.data.model.NutritionFacts
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.result.NutritionFactsView
import com.example.nutripal.ui.component.result.RecommendationCard
import com.example.nutripal.ui.screen.scan.result.ResultScreen
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScanDetailsScreen(
    navController: NavController,
    viewModel: HistoryScanDetailsViewModel = hiltViewModel(),
    documentId: String
) {
    // Fetch details when screen is first loaded
    LaunchedEffect(documentId) {
        viewModel.fetchHistoryDetails(documentId)
    }

    // Collect states from ViewModel
    val nutritionFacts by viewModel.nutritionFacts.collectAsState()
    val recommendation by viewModel.recommendation.collectAsState()
    val imageUri by viewModel.imageUri.collectAsState()

    MainStatusBar()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Result",
                        color = Primary,
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("history_scan") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // Check if all required data is available
        if (nutritionFacts != null && recommendation != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Main content scrollable
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Display the image with a 3:4 aspect ratio
                    imageUri?.let { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = "Scanned food image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(364.dp)
                                .padding(bottom = 16.dp),
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.ic_empty_history)
                        )
                    } ?: run {
                        Text(
                            text = "Image not available",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Display the Nutrition Facts
                    nutritionFacts?.let {
                        NutritionFactsView(it)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Display recommendation
                    RecommendationCard(recommendation ?: "No recommendation available")

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        } else {
            // Loading state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    val navController = rememberNavController()
    ResultScreen(
        navController = navController,
        nutritionFacts = NutritionFacts(30, 20, 20),
        recommendation = "Rekomendasi: Konsumsi secukupnya"
    )
}
