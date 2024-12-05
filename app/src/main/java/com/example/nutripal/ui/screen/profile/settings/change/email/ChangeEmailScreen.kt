package com.example.nutripal.ui.screen.profile.settings.change.email


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
import com.example.nutripal.ui.component.auth.EmailField
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.navigation.Screen
import com.example.nutripal.ui.theme.NunitoFontFamily
import kotlinx.coroutines.launch

@Composable
fun ChangeEmailScreen(
    navController: NavController,
    viewModel: ChangeEmailViewModel = hiltViewModel()
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
            text = "Ubah Email?",
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFF2A2A2A),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Masukkan email baru untuk menggantikan email sebelumnya.",
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color(0xFF6B6B6B),
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Field Email
        EmailField(
            email = viewModel.email.value,
            onValueChange = {
                viewModel.updateEmail(it)
            },
        )

        Spacer(modifier = Modifier.height(24.dp))

        ToggleGreenButton(
            text = if (viewModel.isProcessing.value) "Mengirim..." else "Lanjut Verifikasi",
            onClick = {
                if (!viewModel.isProcessing.value) {
                    coroutineScope.launch {
                        viewModel.sendVerificationEmail(
                            onSuccess = {
                                navController.navigate(Screen.Profile.route)
                            },
                            onFailure = { error ->
                            }
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = !viewModel.isProcessing.value
        )

        if (viewModel.verificationMessage.value.isNotEmpty()) {
            Text(
                text = viewModel.verificationMessage.value,
                color = if (viewModel.verificationMessage.value.startsWith("Gagal")) Color.Red else Color.Green,

            )
        }
    }
}

@Preview
@Composable
fun PreviewChangeEmailScreen() {
    ChangeEmailScreen(navController = rememberNavController())
}
