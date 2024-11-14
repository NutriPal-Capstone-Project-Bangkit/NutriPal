package com.example.nutripal.ui.custom

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutripal.ui.theme.Disabled
import com.example.nutripal.ui.theme.NunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomNameInput(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            "Nama Lengkap",
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = {
                // Tetap izinkan karakter selain huruf atau spasi
                onValueChange(it)
            },
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            isError = isError,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = if (isError) Color.Red else Disabled,
                focusedBorderColor = if (isError) Color.Red else Disabled
            ),
            textStyle = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        )

        if (isError) {
            Text(
                text = "Nama hanya boleh berisi huruf dan spasi.",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
