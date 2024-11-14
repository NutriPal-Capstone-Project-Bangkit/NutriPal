package com.example.nutripal.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutripal.R
import com.example.nutripal.ui.component.ToggleGreenButton

@Composable
fun PersonalDetailsSavedScreen(
) {
    val isButtonEnabled = true

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,  // Menempatkan elemen-elemen dari atas
        horizontalAlignment = Alignment.CenterHorizontally // Memastikan gambar dan tombol berada di tengah
    ) {

        // image
        Image(
            painter = painterResource(id = R.drawable.ic_saved_personal),
            contentDescription = "Personal Details Saved",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally) // Menempatkan gambar di tengah
                .padding(bottom = 32.dp)
        )

        // title
        Text(
            text = "Data berhasil disimpan",
            color = Color(0xFF000000),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = com.example.nutripal.ui.theme.NunitoFontFamily, // Pastikan font Nunito terdefinisi
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        // subtitle
        Text(
            text = "Profil kamu sudah siap. Nikmati pengalaman personal di NutriPal!",
            color = Color(0xFF2A2A2A), // Ganti warna teks sesuai kebutuhan
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = com.example.nutripal.ui.theme.NunitoFontFamily, // Pastikan font Nunito terdefinisi
            modifier = Modifier.padding(bottom = 32.dp),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(64.dp))

        // button
        ToggleGreenButton(
            text = "Lanjut",
            enabled = isButtonEnabled,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalDetailsSavedScreenPreview() {
    PersonalDetailsSavedScreen()
    }
