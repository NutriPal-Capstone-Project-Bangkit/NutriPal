package com.example.nutripal.ui.screen.home.dailytrack

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
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
import com.example.nutripal.ui.navigation.Screen
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary

@Composable
fun DailyNutritionScreen(
    navController: NavController,
) {
    Scaffold(
        topBar = {
            DailyNutritionTopBar(
                navController = navController,
                onCalendarClick = { /* Handle calendar click */ }
            )
        },
        content = { padding ->
            DailyNutritionContent(
                modifier = Modifier.padding(padding)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DailyNutritionTopBar(
    navController: NavController,
    onCalendarClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Daily Nutritions",
                color = Primary,
                style = TextStyle(
                    fontFamily = NunitoFontFamily,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onCalendarClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "Calendar"
                )
            }
        },
        modifier = Modifier.background(
            MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    )
}

@Composable
private fun DailyNutritionContent(
    modifier: Modifier = Modifier
) {
}