package com.example.nutripal.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.nutripal.R
import com.example.nutripal.ui.component.ToggleGreenButton
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary

@Composable
fun VerificationScreen(
    email: String = "m375237@gmail.com",
    onResendClick: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back button and title bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 32.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.align(Alignment.CenterStart)
            )

            Text(
                text = "Verifikasi",
                style = TextStyle(
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    color = Primary
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Verification illustration
        Image(
            painter = painterResource(id = R.drawable.ic_email_verif),
            contentDescription = "Verification Illustration",
            modifier = Modifier
                .fillMaxWidth(0.8f) // To match the button width
                .heightIn(min = 180.dp, max = 300.dp) // Defines a taller image
                .padding(bottom = 32.dp)
        )

        // Verification title
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

        // Verification message with email
        // Verification message with email in a single line within a Column
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
                        append(email)
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

        // Resend button
        ToggleGreenButton(
            text = "Kirim Ulang",
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onResendClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Help text
        Text(
            text = "Tidak menemukan email verifikasi? Tekan tombol untuk kirim ulang.",
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
fun VerificationScreenPreview() {
    VerificationScreen()
}
