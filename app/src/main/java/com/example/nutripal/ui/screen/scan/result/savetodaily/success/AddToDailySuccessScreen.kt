package com.example.nutripal.ui.screen.scan.result.savetodaily.success

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.component.home.HomeStatusBar

@Composable
fun AddToDailySuccessScreen(navController: NavController
) {
    HomeStatusBar()
    val isButtonEnabled = true

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // image
        Image(
            painter = painterResource(id = R.drawable.daily_nutrition_saved),
            contentDescription = "Daily Nutrition Saved",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 32.dp)
        )

        // title
        Text(
            text = "Tersimpan!",
            color = Color(0xFF000000),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = com.example.nutripal.ui.theme.NunitoFontFamily,
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        // subtitle
        Text(
            text = "Tambah Nutrisi harian berhasil ditambahkan. Kamu bisa cek ke dalam nutrisi harianmu.",
            color = Color(0xFF2A2A2A),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = com.example.nutripal.ui.theme.NunitoFontFamily,
            modifier = Modifier.padding(bottom = 24.dp).padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        // button
        Button(
            onClick = { navController.navigate("camera_screen") },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF4CAF50)
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, Color(0xFF4CAF50))
        ) {
            Text(
                text = "Scan Lagi",
                fontSize = 16.sp,
                color = Color(0xFF4CAF50)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ToggleGreenButton(
            text = "Nutrisi Harian",
            enabled = isButtonEnabled,
            onClick = {navController.navigate("daily_track")},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddToDailyNutritionScreenPreview() {
    val navController = rememberNavController()
    AddToDailySuccessScreen(navController)
}
