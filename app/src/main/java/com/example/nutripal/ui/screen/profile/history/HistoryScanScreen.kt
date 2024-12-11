package com.example.nutripal.ui.screen.profile.history

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.data.local.NutritionDatabase.Companion.getDatabase
import com.example.nutripal.data.repository.HistoryRepository
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.profile.history.HistoryScanItem
import com.example.nutripal.ui.navigation.Screen
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScanScreen(
    onBackClick: () -> Unit,
    navController: NavController,
    viewModel: HistoryScanViewModel
) {
    val historyItems by viewModel.historyItems
    val isLoading by viewModel.isLoading

    MainStatusBar()

    LaunchedEffect(Unit) {
        viewModel.fetchHistoryItems()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "History",
                        color = Primary,
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                        navController.navigate(Screen.Profile.route)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_loading_bot),
                    contentDescription = "Loading",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Unspecified
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
            ) {
                items(historyItems.size) { index ->
                    val item = historyItems[index]
                    HistoryScanItem(
                        imageUrl = item.imageUrl,
                        title = item.title,
                        dateTime = item.dateTime,
                        carbs = item.carbs,
                        protein = item.protein,
                        fat = item.fat,
                        onClick = {
                            navController.navigate("history_details/${item.documentId}")
                        },
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewHistoryScreen() {
    val navController = rememberNavController()

    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    val context = LocalContext.current
    val historyDao = getDatabase(context).historyDao()

    val historyRepository = HistoryRepository(firestore = firestore, historyDao = historyDao, auth = auth)

    val historyScanViewModel = HistoryScanViewModel(historyRepository = historyRepository)

    HistoryScanScreen(

        onBackClick = { /* Action saat tombol kembali ditekan */ },
        navController = navController,
        historyScanViewModel
    )
}
