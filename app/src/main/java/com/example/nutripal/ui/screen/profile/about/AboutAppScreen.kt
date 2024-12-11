package com.example.nutripal.ui.screen.profile.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Tentang Aplikasi",
                        color = Color.White, // Set text color to white
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Primary
                ),
                modifier = Modifier.background(Primary),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary)
                .padding(paddingValues)
        ) {
            // Center image (about_app.png)
            Image(
                painter = painterResource(id = R.drawable.about_app),
                contentDescription = "About App Image",
                modifier = Modifier
                    .size(320.dp)
                    .align(Alignment.Center)
                    .padding(16.dp)
            )

            // Left logo (ic_spoon) at the absolute top left corner, below the TopAppBar
            Image(
                painter = painterResource(id = R.drawable.ic_spoon),
                contentDescription = "Spoon Logo Left",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-12).dp)
                    .padding(top = 20.dp)
                    .size(200.dp)
            )

            // Right logo (ic_spoon) at the absolute bottom right corner, extended beyond normal bounds
            Image(
                painter = painterResource(id = R.drawable.ic_spoon),
                contentDescription = "Spoon Logo Right",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 62.dp)
                    .size(200.dp)
            )
        }
    }
}

@Preview
@Composable
fun preview() {
    AboutAppScreen(navController = rememberNavController())
}
