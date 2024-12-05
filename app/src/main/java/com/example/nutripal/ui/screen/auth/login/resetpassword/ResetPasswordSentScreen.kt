package com.example.nutripal.ui.screen.auth.login.resetpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.navigation.Screen
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordSentScreen(
    email: String,
    navController: NavController,
    onBack: () -> Unit = {},
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
) {
    var remainingTime by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var inputEmail by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.updateEmail(email)
    }

    LaunchedEffect(remainingTime) {
        while (remainingTime > 0) {
            delay(1000)
            remainingTime--
            if (remainingTime == 0) canResend = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 32.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { onBack() }
            )

            Text(
                text = "Lupa Password",
                style = TextStyle(
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    color = Primary
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_email_verif),
            contentDescription = "Verification Illustration",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .heightIn(min = 180.dp, max = 300.dp)
                .padding(bottom = 32.dp)
        )

        Text(
            text = "Periksa Emailmu!",
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val currentEmail = email

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = buildAnnotatedString {
                    append("Kami sudah kirim link reset password ke ")
                    withStyle(style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Primary
                    ).toSpanStyle()) {
                        append(currentEmail)
                    }
                },
                style = TextStyle(
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize =  14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Buka dan baca instruksi yang kami berikan.",
                style = TextStyle(
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(108.dp))

        ToggleGreenButton(
            text = if (remainingTime > 0) "Kirim Ulang (${remainingTime}s)" else "Kirim Ulang",
            modifier = Modifier.fillMaxWidth(),
            enabled = canResend,
            onClick = {
                coroutineScope.launch {
                    val success = viewModel.sendPasswordResetEmail()
                    if (success) {
                        remainingTime = 60
                        canResend = false
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (remainingTime > 0)
                "Tunggu ${remainingTime} detik untuk mengirim ulang."
            else
                "Tidak menemukan email reset password? Tekan tombol untuk kirim ulang.",
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // Menambahkan teks untuk login setelah berhasil reset password
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sudah berhasil ganti password? Login sekarang!",
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Primary,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // Arahkan pengguna ke halaman login
                    navController.navigate(Screen.Login.route)
                }
        )
    }
}

@Preview
@Composable
fun preview(){
    ResetPasswordSentScreen(email = "test@gmail.com", navController = rememberNavController())
}