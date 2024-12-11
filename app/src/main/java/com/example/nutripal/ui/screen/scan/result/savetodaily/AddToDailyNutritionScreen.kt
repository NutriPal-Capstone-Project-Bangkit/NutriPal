@file:Suppress("DEPRECATION")

package com.example.nutripal.ui.screen.scan.result.savetodaily

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.data.model.NutritionFacts
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.component.result.RecommendationCard
import com.example.nutripal.ui.custom.scan.result.addtodaily.CustomNutritionFactsEdit
import com.example.nutripal.ui.custom.scan.result.addtodaily.CustomProductDropdown
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutripal.ui.component.home.chatbot.LoadingIndicator
import com.example.nutripal.ui.component.home.dailytrack.addtodaily.AddToDailyRecommendation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToDailyNutritionScreen(
    navController: NavController,
    imageUri: Uri? = null,
    carbohydrate: Int = 0,
    protein: Int = 0,
    fat: Int = 0,
    recommendation: String = "",
    viewModel: AddToDailyViewModel = hiltViewModel()
)  {
    MainStatusBar()
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val selectedProduct by viewModel.selectedProduct.collectAsState()

    val isRefreshEnabled by viewModel.isRefreshEnabled.collectAsState()
    val refreshedRecommendation by viewModel.recommendation.collectAsState()

    var carbohydrateState by remember { mutableIntStateOf(carbohydrate) }
    var proteinState by remember { mutableIntStateOf(protein) }
    var fatState by remember { mutableIntStateOf(fat) }

    var isDropdownExpanded by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading.collectAsState()

    val recommendation = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("recommendation") ?: ""

    val saveState by viewModel.saveState.collectAsState()

    LaunchedEffect(carbohydrateState, proteinState, fatState) {
        viewModel.updateRefreshEnabled(
            initialCarbohydrate = carbohydrate,
            currentCarbohydrate = carbohydrateState,
            initialProtein = protein,
            currentProtein = proteinState,
            initialFat = fat,
            currentFat = fatState
        )
    }

    LaunchedEffect(saveState) {
        if (saveState == true) {
            navController.navigate("daily_nutrition_success")
        }
    }

    LaunchedEffect(key1 = Unit) {
        val storedImageUri = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Uri>("cropped_image_uri")

        storedImageUri?.let { uri ->
            try {
                // Convert URI to Bitmap
                val loadedBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                bitmap = loadedBitmap
            } catch (e: Exception) {
                println("Error loading bitmap: ${e.message}")
                Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            println("No cropped image URI found")
        }
    }

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
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (saveState == false) {
                    Image(
                        painter = painterResource(id = R.drawable.failed_add_to_daily),
                        contentDescription = "Failed to add to daily nutrition",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
                CustomProductDropdown(
                    selectedProduct = selectedProduct,
                    isExpanded = isDropdownExpanded,
                    onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
                    onProductSelected = { product ->
                        viewModel.updateProduct(product)
                    },
                    onDismiss = { isDropdownExpanded = false }
                )

                Spacer(modifier = Modifier.height(16.dp))

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

                CustomNutritionFactsEdit(
                    nutritionFacts = NutritionFacts(carbohydrateState, proteinState, fatState),
                    onCarbohydrateChange = { carbohydrateState = it },
                    onProteinChange = { proteinState = it },
                    onFatChange = { fatState = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Display recommendation

                if (isLoading) {
                    LoadingIndicator(
                        text = "Sedang memikirkan jawaban...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                } else {
                    AddToDailyRecommendation(
                        recommendation = refreshedRecommendation.ifEmpty { recommendation },
                        isRefreshEnabled = isRefreshEnabled,
                        onRefreshClick = {
                            viewModel.refreshRecommendation(
                                NutritionFacts(
                                    carbohydrate = carbohydrateState,
                                    protein = proteinState,
                                    fat = fatState
                                )
                            )
                        }
                    )
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
                    text = "Simpan",
                    enabled = selectedProduct.isNotEmpty(),
                    onClick = {
                        viewModel.saveNutritionFacts(
                            NutritionFacts(
                                carbohydrate = carbohydrateState,
                                protein = proteinState,
                                fat = fatState
                            )
                        )
                        viewModel.saveDailyNutrition(imageUri = imageUri.toString(),
                            recommendation = refreshedRecommendation.ifEmpty { recommendation })
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview2(){
    AddToDailyNutritionScreen(navController = rememberNavController())
}
