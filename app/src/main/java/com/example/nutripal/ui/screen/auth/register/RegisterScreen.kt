@file:Suppress("FunctionName")

package com.example.nutripal.ui.screen.auth.register

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.ui.component.auth.AuthHeaderImage
import com.example.nutripal.ui.component.auth.EmailField
import com.example.nutripal.ui.component.LoadingAnimation
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.auth.PasswordField
import com.example.nutripal.ui.component.auth.regist.StatusDialog
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.component.auth.CustomCanvas
import com.example.nutripal.ui.component.auth.regist.UsernameField
import com.example.nutripal.ui.navigation.Screen
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.ui.theme.darkGray

@Composable
fun RegisterScreen(navController: NavController, context: Context) {
    MainStatusBar()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    val viewModel: RegisterViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (showDialog) {
        StatusDialog(
            showDialog = showDialog,
            isSuccess = isSuccess,
            message = dialogMessage,
            onDismiss = {
                showDialog = false
                if (isSuccess) {
                    navController.navigate("email_verification")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA))
    ) {
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left side - Image and Header
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    AuthHeaderImage(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(bottom = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = "Ayo Bergabung!",
                        fontSize = 20.sp,
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )

                    Text(
                        text = buildAnnotatedString {
                            append("Daftar sekarang untuk mulai lacak dan capai\n")
                            append("tujuan sehatmu!")
                        },
                        fontFamily = NunitoFontFamily,
                        fontSize = 14.sp,
                        color = darkGray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Right side - Form Fields
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    UsernameField(
                        value = username,
                        onValueChange = { username = it},
                        placeholderText = "Masukkan Username",
                        isLoading = isLoading,
                        onNextFocus = {}
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    EmailField(
                        email = email,
                        onValueChange = { email = it },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PasswordField(
                        password = password,
                        label = "Masukkan Password",
                        onValueChange = { password = it },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PasswordField(
                        password = confirmPassword,
                        label = "Konfirmasi Password",
                        onValueChange = { confirmPassword = it },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ToggleGreenButton(
                        text = "Daftar",
                        enabled = !isLoading && username.isNotEmpty() && email.isNotEmpty() &&
                                password.isNotEmpty() && password == confirmPassword,
                        onClick = {
                            viewModel.register(email, password, confirmPassword)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = buildAnnotatedString {
                            append("sudah punya akun? ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Masuk")
                            }
                        },
                        fontFamily = NunitoFontFamily,
                        fontSize = 14.sp,
                        color = Primary,
                        modifier = Modifier.clickable(enabled = !isLoading) {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.EmailVerification.route) { inclusive = true }
                            }
                        }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AuthHeaderImage(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.TopCenter)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 60.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(top = 70.dp),
                            text = "Ayo Bergabung!",
                            fontFamily = NunitoFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 5.dp),
                            text = buildAnnotatedString {
                                append("Daftar sekarang untuk mulai lacak dan capai\n")
                                append("tujuan sehatmu!")
                            },
                            fontFamily = NunitoFontFamily,
                            fontSize = 14.sp,
                            color = darkGray
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(36.dp))

                    UsernameField(
                        value = username,
                        onValueChange = { username = it},
                        placeholderText = "Masukkan Username",
                        isLoading = isLoading,
                        onNextFocus = {}
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    EmailField(
                        email = email,
                        onValueChange = { email = it },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PasswordField(
                        password = password,
                        label = "Masukkan Password",
                        onValueChange = { password = it },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PasswordField(
                        password = confirmPassword,
                        label = "Konfirmasi Password",
                        onValueChange = { confirmPassword = it },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ToggleGreenButton(
                        text = "Daftar",
                        enabled = !isLoading && username.isNotEmpty() && email.isNotEmpty() &&
                                password.isNotEmpty() && password == confirmPassword,
                        onClick = {
                            viewModel.register(email, password, confirmPassword)
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 40.dp)
                        .zIndex(1f)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("sudah punya akun? ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = NunitoFontFamily )) {
                                append("Masuk")
                            }
                        },
                        fontSize = 14.sp,

                        color = Color.White,
                        modifier = Modifier.clickable(enabled = !isLoading) {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.EmailVerification.route) { inclusive = true }
                            }
                        }
                    )
                }

                CustomCanvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }

        // Loading Overlay (Unchanged)
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(enabled = false) { }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = 40.dp)
                ) {
                    LoadingAnimation(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(160.dp)
                    )
                }
            }
        }
    }

    // LaunchedEffects remain the same
    LaunchedEffect(viewModel.errorMessage.value) {
        if (viewModel.errorMessage.value.isNotEmpty() && !viewModel.isRegisterSuccess.value) {
            isSuccess = false
            dialogMessage = viewModel.errorMessage.value
            showDialog = true
        }
    }

    LaunchedEffect(viewModel.isRegisterSuccess.value) {
        if (viewModel.isRegisterSuccess.value) {
            isSuccess = true
            dialogMessage = "Registrasi berhasil!"
            showDialog = true
        }
    }
}

@Preview
@Composable
fun Preview(){
    val context = LocalContext.current
    RegisterScreen(rememberNavController(), context = context)
}
