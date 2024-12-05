package com.example.nutripal.ui.screen.personaldetails.saved

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.nutripal.ui.navigation.Screen

@Composable
fun PersonalDetailsSavedScreen(navController: NavController
) {
    HomeStatusBar()
    val isButtonEnabled = true
    val context = LocalContext

    BackHandler {
        (context as? Activity)?.moveTaskToBack(true)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // image
        Image(
            painter = painterResource(id = R.drawable.ic_saved_personal),
            contentDescription = "Personal Details Saved",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 32.dp)
        )

        // title
        Text(
            text = "Data berhasil disimpan",
            color = Color(0xFF000000),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = com.example.nutripal.ui.theme.NunitoFontFamily,
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        // subtitle
        Text(
            text = "Profil kamu sudah siap. Nikmati pengalaman personal di NutriPal!",
            color = Color(0xFF2A2A2A),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = com.example.nutripal.ui.theme.NunitoFontFamily,
            modifier = Modifier.padding(bottom = 32.dp),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(64.dp))

        // button
        ToggleGreenButton(
            text = "Lanjut",
            enabled = isButtonEnabled,
            onClick = {navController.navigate(Screen.Home.route)},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalDetailsSavedScreenPreview() {
    val navController = rememberNavController()
    PersonalDetailsSavedScreen(navController)
}
