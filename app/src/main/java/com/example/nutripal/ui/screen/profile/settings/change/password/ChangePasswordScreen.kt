package com.example.nutripal.ui.screen.profile.settings.change.password

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.ui.component.auth.PasswordField
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.navigation.Screen
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.screen.auth.login.resetpassword.ForgotPasswordViewModel
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordScreen(
    navController: NavController,
    onBack: () -> Unit = {},
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "Kembali",
            modifier = Modifier
                .size(24.dp)
                .clickable { navController.navigate("settings") },
            tint = Color(0xFF2A2A2A)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Password Baru",
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color(0xFF2A2A2A),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Masukkan password baru untuk menggantikan password\nsebelumnya.",
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color(0xFF6B6B6B),
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Password Baru
        PasswordField(
            password = viewModel.newPassword.value,
            label = "Masukkan Password Baru",
            onValueChange = { viewModel.updateNewPassword(it) },
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Konfirmasi Password
        PasswordField(
            password = viewModel.confirmPassword.value,
            label = "Konfirmasi Password",
            onValueChange = { viewModel.updateConfirmPassword(it) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        ToggleGreenButton(
            text = "Perbarui Password",
            onClick = {
                coroutineScope.launch {
                    viewModel.changePassword(
                        onSuccess = {
                            navController.navigate("settings")
                        },
                        onFailure = { errorMessage ->
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        if (viewModel.changePasswordMessage.value.isNotEmpty()) {
            Text(
                text = viewModel.changePasswordMessage.value,
                color = if (viewModel.changePasswordMessage.value.startsWith("Gagal")) Color.Red else Color.Green,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun preview() {
    ChangePasswordScreen(navController = rememberNavController())
}
