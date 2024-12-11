package com.example.nutripal.ui.screen.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.component.auth.regist.StatusDialog
import com.example.nutripal.ui.navigation.Screen
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun VerificationScreen(
    navController: NavController,
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit = {
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.EmailVerification.route) { inclusive = true }
        }
    }
) {
    val isEmailVerified by viewModel.isEmailVerified
    var remainingTime by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }
    val showVerificationDialog by viewModel.showVerificationDialog

    MainStatusBar()

    LaunchedEffect(Unit) {
        while (!isEmailVerified) {
            viewModel.checkEmailVerification()
            delay(2000)
        }
    }

    LaunchedEffect(remainingTime) {
        while (remainingTime > 0) {
            delay(1000)
            remainingTime--
            if (remainingTime == 0) canResend = true
        }
    }

    StatusDialog(
        showDialog = showVerificationDialog,
        isSuccess = true,
        message = "Email berhasil terverifikasi.Yuk, login sekarang!",
        onDismiss = {
            viewModel.showVerificationDialog.value = false
            onNavigateToLogin()
        }
    )

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
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(20.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )

            Text(
                text = "Verifikasi",
                style = TextStyle(
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
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
            text = "Verifikasi Emailmu!",
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val currentEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Kami sudah kirim link verifikasi ke ")
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
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Buka dan klik link verifikasi untuk mengaktifkan akun.",
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
                viewModel.resendVerificationEmail()
                remainingTime = 60 // Reset timer ke 60 detik
                canResend = false
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (remainingTime > 0)
                "Tunggu ${remainingTime} detik untuk mengirim ulang."
            else
                "Tidak menemukan email verifikasi? Tekan tombol untuk kirim ulang.",
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
    }
}

@Preview(showBackground = true)
@Composable
fun preview(){
    val navController = rememberNavController()
    val registerViewModel: RegisterViewModel = hiltViewModel()

    VerificationScreen(
        navController = navController,
        viewModel = registerViewModel,
        onNavigateToLogin = {
        }
    )
}