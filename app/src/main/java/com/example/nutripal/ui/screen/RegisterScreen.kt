package com.example.nutripal.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.nutripal.ui.components.AuthHeaderImage
import com.example.nutripal.ui.components.EmailField
import com.example.nutripal.ui.components.PasswordField
import com.example.nutripal.ui.custom.CustomCanvas
import com.example.nutripal.ui.custom.CustomCheckbox
import com.example.nutripal.ui.theme.Disabled
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.ui.theme.darkGray
import com.example.nutripal.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), navController: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
                    text = "Ayo Bergabung!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary
                )

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 5.dp),
                    text = buildAnnotatedString {
                        append("Daftar sekarang untuk mulai lacak dan capai\n")
                        append("tujuan sehatmu!")
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
            Spacer(modifier = Modifier.height(72.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Masukkan Username", fontFamily = NunitoFontFamily, fontWeight = FontWeight.Medium) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFEFEFEF),
                    focusedTextColor = Color(0xFF2A2A2A),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            EmailField(email = email, onValueChange = { email = it })

            Spacer(modifier = Modifier.height(16.dp))

            PasswordField(password = password, label = "Masukkan Password", onValueChange = { password = it })

            Spacer(modifier = Modifier.height(16.dp))

            PasswordField(password = confirmPassword, label = "Konfirmasi Password", onValueChange = { confirmPassword = it })
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
                    Text(text = "Setuju dengan syarat dan ketentuan",
                        fontSize = 14.sp,
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.register() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    disabledContainerColor = Disabled
                ),
                enabled = username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Daftar", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

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
                    append("sudah punya akun? ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Masuk")
                    }
                },
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.clickable {
                    navController.navigate("login")
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
