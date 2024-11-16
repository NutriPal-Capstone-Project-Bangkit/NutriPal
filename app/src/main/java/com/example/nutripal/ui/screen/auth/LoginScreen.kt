@file:Suppress("FunctionName")

package com.example.nutripal.ui.screen.auth

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nutripal.R
import com.example.nutripal.ui.component.AuthHeaderImage
import com.example.nutripal.ui.component.EmailField
import com.example.nutripal.ui.component.GoogleSignInButton
import com.example.nutripal.ui.component.PasswordField
import com.example.nutripal.ui.component.ToggleGreenButton
import com.example.nutripal.ui.custom.CustomCanvas
import com.example.nutripal.ui.custom.CustomCheckbox
import com.example.nutripal.ui.theme.Disabled
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.ui.theme.Secondary
import com.example.nutripal.ui.theme.darkGray
import com.example.nutripal.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel(), navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Initialize Google Sign In on first composition
    LaunchedEffect(Unit) {
        viewModel.initializeGoogleSignIn(context)
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
                .padding(top=16.dp)
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
                label = "Masukkan Password",
                onValueChange = { viewModel.updatePassword(it) }
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
                    Text(text = "Ingat Saya",
                        fontSize = 14.sp,
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Medium)
                }
                Text(text = "Lupa Password",
                    color = Secondary,
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            ToggleGreenButton(
                text = "Masuk",
                enabled = viewModel.isLoginEnabled,
                onClick = {
                    viewModel.login(navController)
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
    val errorMessage by viewModel.errorMessage
    if (errorMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_LONG).show()
    }
}