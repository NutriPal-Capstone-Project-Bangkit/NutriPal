package com.example.nutripal.ui.screen

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.nutripal.R
import com.example.nutripal.ui.components.AuthHeaderImage
import com.example.nutripal.ui.components.EmailField
import com.example.nutripal.ui.components.PasswordField
import com.example.nutripal.ui.custom.CustomCanvas
import com.example.nutripal.ui.custom.CustomCheckbox
import com.example.nutripal.ui.theme.Disabled
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.ui.theme.Secondary
import com.example.nutripal.ui.theme.darkGray
import com.example.nutripal.viewmodel.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }

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

            EmailField(email = email, onValueChange = { email = it })

            Spacer(modifier = Modifier.height(16.dp))

            PasswordField(password = password, label = "Masukkan Password", onValueChange = { password = it })

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

            Button(
                onClick = { viewModel.login() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    disabledContainerColor = Disabled
                ),
                enabled = email.isNotEmpty() && password.isNotEmpty(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Masuk", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                thickness = 0.9.dp,
                color = Disabled
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),
                border = BorderStroke(0.75.dp, Color.Gray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Masuk dengan Google",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Masuk dengan Google", fontSize = 16.sp)
            }

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
}
