package com.example.nutripal.ui.screen.scan.result

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.data.model.NutritionFacts
import com.example.nutripal.data.repository.OCRRepository
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.component.home.chatbot.LoadingIndicator
import com.example.nutripal.ui.component.result.NutritionFactsView
import com.example.nutripal.ui.component.result.RecommendationCard
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    navController: NavController,
    nutritionFacts: NutritionFacts,
    recommendation: String
) {

    MainStatusBar()
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val viewModel: ResultScreenViewModel = viewModel { ResultScreenViewModel(OCRRepository()) }

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
                    IconButton(onClick = { navController.navigate("camera_screen") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
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
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Adjust padding
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
                RecommendationCard(recommendation)

                Spacer(modifier = Modifier.height(100.dp))
            }

            // Box wrapping the button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
                    .padding(vertical = 16.dp) // Adjust padding inside the box
            ) {
                ToggleGreenButton(
                    text = "Tambah ke Konsumsi Hari Ini",
                    onClick = { /* Add logic for button click */ },
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
fun Preview() {
    val navController = rememberNavController()
    ResultScreen(
        navController = navController,
        nutritionFacts = NutritionFacts(30, 20, 20),
        recommendation = "Rekomendasi: Konsumsi secukupnya"
    )
}
