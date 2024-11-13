@file:Suppress("FunctionName")

package com.example.nutripal.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.nutripal.R
import com.example.nutripal.ui.component.PageIndicator
import com.example.nutripal.ui.component.ToggleGreenButton
import com.example.nutripal.ui.theme.Primary

@Composable
fun PersonalDetailsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Page Indicator
        PageIndicator(currentPage = 0, pageCount = 2)

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "Pribadimu",
            style = MaterialTheme.typography.titleMedium,
            color = Primary,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Subtitle
        Text(
            text = "Isi Profil Kamu\nLengkapi data dirimu agar sistem bisa memberikan pengalaman terbaik!",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            lineHeight = 20.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Nama Lengkap Field
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Nama Lengkap") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Jenis Kelamin Field
        OutlinedTextField(
            value = "Pria",
            onValueChange = {},
            label = { Text("Jenis Kelamin") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Handle dropdown menu for options */ },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon"
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(0.5.dp, color = Color(0xFFE1E1E1), shape = MaterialTheme.shapes.medium)
                .clickable { /* Handle image picker */ },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.upload_profile),
                contentDescription = "Upload Profile Icon",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
                    .zIndex(1f),
                contentScale = ContentScale.Crop
            )
        }

        // Button Lanjut
        Column(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            ToggleGreenButton(
                text = "Lanjut",
                enabled = true, // atau set sesuai kondisi yang diinginkan
                onClick = { /* Handle button click */ }
            )

        }
    }
}

    @Preview(showBackground = true)
    @Composable
    fun PersonalDetailsScreenPreview() {
        PersonalDetailsScreen()
    }
