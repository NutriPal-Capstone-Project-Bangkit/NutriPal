package com.example.nutripal.ui.screen.auth.login.resetpassword


import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.nutripal.ui.component.home.HomeStatusBar
import com.example.nutripal.ui.theme.Secondary

@Composable
fun ResetPasswordFailedScreen(navController: NavController
) {
    HomeStatusBar()

    val isButtonEnabled = true
    val context = LocalContext.current

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
            painter = painterResource(id = R.drawable.reset_password_failed_frame),
            contentDescription = "Personal Details Saved",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 32.dp),
        )

        // title
        Text(
            text = "Data gagal disimpan",
            color = Color(0xFF000000),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = com.example.nutripal.ui.theme.NunitoFontFamily,
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        // subtitle
        Text(
            text = "Oops, sepertinya password baru gagal terganti. Pastikan\nemail yang dimasukkan terdaftar ya! Yuk, coba lagi\ndengan menekan tombol di bawah!",
            color = Color(0xFF2A2A2A),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = com.example.nutripal.ui.theme.NunitoFontFamily,
            modifier = Modifier.padding(bottom = 32.dp),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(64.dp))

        // button
        Button(
            onClick = {
                navController.navigate("forgot_password_screen")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Secondary,
            ),
            border = BorderStroke(1.dp, Secondary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Coba Lagi",
                color = Secondary, // Warna teks
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewFailed() {
    ResetPasswordFailedScreen(navController = rememberNavController())
}
