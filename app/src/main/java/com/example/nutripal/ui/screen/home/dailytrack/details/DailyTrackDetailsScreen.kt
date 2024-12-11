package com.example.nutripal.ui.screen.home.dailytrack.details

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.nutripal.R
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.component.result.NutritionFactsView
import com.example.nutripal.ui.component.result.RecommendationCard
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.viewmodel.compose.viewModel

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTrackDetailsScreen(
    navController: NavController,
    documentId: String,
    viewModel: DailyTrackDetailsViewModel = viewModel()
) {
    // Fetch details when the screen is first loaded
    LaunchedEffect(documentId) {
        viewModel.fetchHistoryDetails(documentId)
    }

    // Collect states from ViewModel
    val nutritionFacts by viewModel.nutritionFacts.collectAsStateWithLifecycle()
    val recommendation by viewModel.recommendation.collectAsStateWithLifecycle()
    val imageUri by viewModel.imageUri.collectAsStateWithLifecycle()

    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Convert URI to Bitmap
    remember(imageUri) {
        imageUri?.let { uri ->
            try {
                // Convert URI to Bitmap
                val loadedBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                bitmap = loadedBitmap
            } catch (e: Exception) {
                println("Error loading bitmap: ${e.message}")
                Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Check if data is loaded
    if (nutritionFacts == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    MainStatusBar()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Hasil",
                        color = Primary,
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
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

                // Use non-null assertion as we've already checked for null above
                NutritionFactsView(nutritionFacts!!)

                Spacer(modifier = Modifier.height(16.dp))

                // Display recommendation
                RecommendationCard(recommendation ?: "No recommendation available")

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
                    enabled = false,
                    text = "Telah tersimpan ke Konsumsi Hari Ini",
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}