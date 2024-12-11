@file:Suppress("FunctionName")

package com.example.nutripal.ui.screen.auth.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.nutripal.ui.component.LoadingAnimation
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.auth.AuthHeaderImage
import com.example.nutripal.ui.component.auth.CustomCanvas
import com.example.nutripal.ui.component.auth.CustomCheckbox
import com.example.nutripal.ui.component.auth.EmailField
import com.example.nutripal.ui.component.auth.PasswordField
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.component.auth.login.GoogleSignInButton
import com.example.nutripal.ui.navigation.Screen
import com.example.nutripal.ui.theme.Disabled
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.ui.theme.Secondary
import com.example.nutripal.ui.theme.darkGray
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@SuppressLint("SuspiciousIndentation")
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel(), navController: NavController) {
    MainStatusBar()

    var isChecked by rememberSaveable { mutableStateOf(false) }
    val isLoading by viewModel.isLoading
    val context = LocalContext.current
    var hasShownError by rememberSaveable { mutableStateOf(false) }
    val errorMessage by viewModel.errorMessage
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty() && !hasShownError) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            hasShownError = true
        }
    }

    // Reset error flag when error is cleared
    LaunchedEffect(errorMessage) {
        if (errorMessage.isEmpty()) {
            hasShownError = false
        }
    }

    // Initialize Google Sign In on first composition
    LaunchedEffect(Unit) {
        viewModel.initializeGoogleSignIn(context)
    }

    LaunchedEffect(Unit) {
        viewModel.checkUserRemembered(navController)
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
                        text = "Masuk ke NutriPal",
                        fontSize = 20.sp,
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )

                    Text(
                        text = buildAnnotatedString {
                            append("Mulai lacak asupan sehatmu dengan masuk\nke akun!")
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                            fontSize = 14.sp,
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.ForgotPassword.route)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ToggleGreenButton(
                        text = "Masuk",
                        enabled = viewModel.isLoginEnabled,
                        onClick = {
                            viewModel.login(navController, rememberMe = isChecked)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 0.9.dp,
                        color = Disabled
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    GoogleSignInButton(
                        onClick = {
                            viewModel.getGoogleSignInIntent()?.let { signInIntent ->
                                launcher.launch(signInIntent)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = buildAnnotatedString {
                            append("belum punya akun? ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = NunitoFontFamily)) {
                                append("Daftar")
                            }
                        },
                        fontFamily = NunitoFontFamily,
                        fontSize = 14.sp,
                        color = Primary,
                        modifier = Modifier.clickable {
                            navController.navigate("register")
                        }
                    )
                }
            }
        } else {
            // Original Portrait Layout
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
                            text = "Masuk ke NutriPal",
                            fontSize = 20.sp,
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 5.dp),
                            text = buildAnnotatedString {
                                append("Mulai lacak asupan sehatmu dengan masuk\nke akun!")
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
                    Spacer(modifier = Modifier.height(64.dp))

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
                            .fillMaxWidth(),
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
                            fontSize = 14.sp,
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.ForgotPassword.route)
                            }
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
                            .fillMaxWidth(),
                        thickness = 0.9.dp,
                        color = Disabled
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    GoogleSignInButton(
                        onClick = {
                            viewModel.getGoogleSignInIntent()?.let { signInIntent ->
                                launcher.launch(signInIntent)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
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
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = NunitoFontFamily)) {
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
        }

        // Loading Overlay
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
}

@Preview
@Composable
fun LoginScreenPreview(){
    LoginScreen(navController = rememberNavController())
}