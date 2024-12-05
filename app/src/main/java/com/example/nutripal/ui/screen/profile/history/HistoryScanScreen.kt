package com.example.nutripal.ui.screen.profile.history

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.profile.history.HistoryScanItem
import com.example.nutripal.ui.navigation.Screen
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScanScreen(
    onBackClick: () -> Unit,
    navController: NavController,
    viewModel: HistoryScanViewModel
) {

    MainStatusBar()
    // Mock data untuk preview
    val mockHistoryItems = listOf(
        HistoryItem(
            imageUrl = "https://via.placeholder.com/150",
            title = "Drink",
            dateTime = "2024-12-18 | 20.20",
            carbs = "18g",
            protein = "3g",
            fat = "4g"
        ),
        HistoryItem(
            imageUrl = "https://via.placeholder.com/150",
            title = "Snack",
            dateTime = "2024-12-17 | 15.00",
            carbs = "12g",
            protein = "5g",
            fat = "2g"
        )
    )

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
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
        ) {
            items(mockHistoryItems) { item ->
                HistoryScanItem(
                    imageUrl = item.imageUrl,
                    title = item.title,
                    dateTime = item.dateTime,
                    carbs = item.carbs,
                    protein = item.protein,
                    fat = item.fat,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


// Data class untuk menyimpan data history scan
data class HistoryItem(
    val imageUrl: String,
    val title: String,
    val dateTime: String,
    val carbs: String,
    val protein: String,
    val fat: String
)

@Preview
@Composable
fun PreviewHistoryScreen() {
    val navController = rememberNavController()
    HistoryScanScreen(
        onBackClick = { /* Action saat tombol kembali ditekan */ },
        navController = navController,
        viewModel = HistoryScanViewModel()
    )
}
