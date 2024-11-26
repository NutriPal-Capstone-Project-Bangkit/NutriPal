@file:Suppress("FunctionName")

package com.example.nutripal.ui.screen.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nutripal.ui.component.LoadingAnimation
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.auth.AuthHeaderImage
import com.example.nutripal.ui.component.auth.CustomCanvas
import com.example.nutripal.ui.component.auth.CustomCheckbox
import com.example.nutripal.ui.component.auth.EmailField
import com.example.nutripal.ui.component.auth.PasswordField
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.component.auth.login.GoogleSignInButton
import com.example.nutripal.ui.theme.Disabled
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.ui.theme.Secondary
import com.example.nutripal.ui.theme.darkGray
import com.example.nutripal.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@SuppressLint("SuspiciousIndentation")
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel(), navController: NavController) {

    MainStatusBar()

    var isChecked by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading
    val context = LocalContext.current

    // Initialize Google Sign In on first composition
    LaunchedEffect(Unit) {
        viewModel.initializeGoogleSignIn(context)
    }

    LaunchedEffect(Unit) {
        if (viewModel.isUserRemembered()) {
            val savedLogin = viewModel.getSavedLogin()
            if (savedLogin != null) {
                viewModel.updateEmail(savedLogin.first)
                viewModel.updatePassword(savedLogin.second)
                viewModel.login(navController, rememberMe = true)
            }
        }
    }


    BackHandler {
        (context as? Activity)?.moveTaskToBack(true)
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let {
                    viewModel.googleSignIn(it, navController)
                }
            } catch (e: ApiException) {
                Toast.makeText(context, "Google Sign-In gagal: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FA))
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
                            .padding(start = 16.dp, top = 70.dp),
                        text = "Masuk ke NutriPal",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 5.dp),
                        text = buildAnnotatedString {
                            append("Mulai lacak asupan sehatmu dengan masuk ke\n")
                            append("akun!")
                        },
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
                Spacer(modifier = Modifier.height(24.dp))

                EmailField(
                    email = viewModel.email.value,
                    onValueChange = { viewModel.updateEmail(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordField(
                    password = viewModel.password.value,
                    onValueChange = { viewModel.updatePassword(it) },
                    label = "Password",
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CustomCheckbox(checked = isChecked, onCheckedChange = { isChecked = it })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ingat Saya",
                            fontSize = 14.sp,
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = "Lupa Password",
                        color = Secondary,
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                ToggleGreenButton(
                    text = "Masuk",
                    enabled = viewModel.isLoginEnabled,
                    onClick = {
                        viewModel.login(navController, rememberMe = isChecked)
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    thickness = 0.9.dp,
                    color = Disabled
                )

                Spacer(modifier = Modifier.height(32.dp))

                GoogleSignInButton(
                    onClick = {
                        viewModel.getGoogleSignInIntent()?.let { signInIntent ->
                            launcher.launch(signInIntent)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp)
                    .zIndex(1f)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("belum punya akun? ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Daftar")
                        }
                    },
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.clickable {
                        navController.navigate("register")
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
    val errorMessage by viewModel.errorMessage
    if (errorMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_LONG).show()
    }
}