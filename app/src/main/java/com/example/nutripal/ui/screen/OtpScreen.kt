package com.example.nutripal.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutripal.R
import com.example.nutripal.ui.custom.CustomOtpTextField
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.ui.theme.darkGray

@Suppress("FunctionName")
@Composable
fun OtpScreen(
    email: String,
    onBackClick: () -> Unit,
    onContinueClick: (String) -> Unit,
    onResendClick: () -> Unit
) {
    var otpValues by remember { mutableStateOf(List(4) { "" }) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Gray
                )
            }
            Text(
                text = "Verifikasi",
                color = Primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Box(modifier = Modifier.width(48.dp))
        }

        // Verification Image
        Image(
            painter = painterResource(id = R.drawable.otp_image),
            contentDescription = "Verification Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(vertical = 32.dp),
            contentScale = ContentScale.Fit
        )

        // Email instruction text
        Text(
            text = buildAnnotatedString {
                append("Kami sudah kirim kode ke gmail kamu. Masuk ke ")
                withStyle(style = SpanStyle(color = Primary)) {
                    append(email)
                }
                append(" untuk mengetahuinya.")
            },
            modifier = Modifier.padding(top = 16.dp),
            color = darkGray
        )

        // OTP Input Fields
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            otpValues.forEachIndexed { index, value ->
                CustomOtpTextField(
                    value = value,
                    onValueChange = { newValue ->
                        if (newValue.length <= 1) {
                            val newOtpValues = otpValues.toMutableList()
                            newOtpValues[index] = newValue
                            otpValues = newOtpValues
                        }
                    }
                )
                if (index < otpValues.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        // Continue Button
        val isComplete = otpValues.all { it.length == 1 }
        Button(
            onClick = {
                if (isComplete) {
                    onContinueClick(otpValues.joinToString(""))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isComplete) MaterialTheme.colorScheme.primary else Color.Gray,
                contentColor = Color.White
            ),
            enabled = isComplete,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Lanjut")
        }

        // Timer and Resend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tidak menerima Kode OTP? ",
                color = Color.Black
            )
            TextButton(
                onClick = onResendClick,
                modifier = Modifier.padding(start = 0.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Kirim Ulang",
                    color = Primary
                )
            }
        }

    }
}

@Suppress("FunctionName")
@Preview(showBackground = true)
@Composable
fun PreviewOtpScreen() {
    OtpScreen(
        email = "user@example.com",
        onBackClick = { /* handle back click */ },
        onContinueClick = { _ -> /* handle continue click with otp */ },
        onResendClick = { /* handle resend click */ }
    )
}
