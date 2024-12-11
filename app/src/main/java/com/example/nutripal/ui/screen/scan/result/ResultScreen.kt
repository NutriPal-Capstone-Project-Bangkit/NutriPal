package com.example.nutripal.ui.screen.scan.result

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.data.model.NutritionFacts
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.component.home.chatbot.LoadingIndicator
import com.example.nutripal.ui.component.result.NutritionFactsView
import com.example.nutripal.ui.component.result.RecommendationCard
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    navController: NavController,
    nutritionFacts: NutritionFacts,
    recommendation: String,
    viewModel: ResultScreenViewModel = hiltViewModel()
) {

    val accessToken by rememberUpdatedState(viewModel.accessToken)

    val generatedRecommendation by viewModel.generatedRecommendation.observeAsState(initial = null)

    if (accessToken != null) {
        LaunchedEffect(accessToken) {
            Log.d("ResultScreen", "Access Token: $accessToken")
        }
    }

    MainStatusBar()
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Retrieve the image URI
    val imageUri = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Uri>("cropped_image_uri")

    // Convert URI to Bitmap
    LaunchedEffect(imageUri) {
        imageUri?.let { uri ->
            try {
                // Convert URI to Bitmap
                val loadedBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                bitmap = loadedBitmap
                // Extract nutrition facts using ViewModel
                viewModel.extractNutritionFromBitmap(loadedBitmap)
            } catch (e: Exception) {
                println("Error loading bitmap: ${e.message}")
                Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            println("No cropped image URI found")
        }
    }

    LaunchedEffect(bitmap) {
        bitmap?.let { viewModel.extractNutritionFromBitmap(it) }
    }

    val currentNutritionFacts by rememberUpdatedState(viewModel.nutritionFacts)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Hasil",
                        color = Primary,
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("camera_screen") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
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
                bitmap?.let { loadedBitmap ->
                    Image(
                        bitmap = loadedBitmap.asImageBitmap(),
                        contentDescription = "Image of food",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(364.dp)
                            .padding(bottom = 16.dp)
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

                currentNutritionFacts?.let {
                    NutritionFactsView(it)
                } ?: Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator(text = "Memproses data nutrisi...")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display recommendation
                generatedRecommendation?.let { recommendation ->
                    Log.d("ResultScreen", "Recommendation received: $recommendation")
                    RecommendationCard(recommendation)
                } ?: run {
                    Log.w("ResultScreen", "Waiting for recommendation")
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator(text = "Memproses rekomendasi...")
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
                    .padding(vertical = 16.dp)
            ) {
                ToggleGreenButton(
                    text = "Tambah ke Konsumsi Hari Ini",
                    enabled = generatedRecommendation != null,
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "cropped_image_uri",
                            imageUri
                        )

                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "recommendation",
                            generatedRecommendation ?: ""
                        )

                        navController.navigate(
                            "add_to_daily_nutrition_screen/" +
                                    "${currentNutritionFacts?.carbohydrate ?: 0}/" +
                                    "${currentNutritionFacts?.protein ?: 0}/" +
                                    "${currentNutritionFacts?.fat ?: 0}/" +
                                    URLEncoder.encode(generatedRecommendation ?: "", StandardCharsets.UTF_8.toString())
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview1() {
    val navController = rememberNavController()
    ResultScreen(
        navController = navController,
        nutritionFacts = NutritionFacts(30, 20, 20),
        recommendation = ""
    )
}
