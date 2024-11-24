package com.example.nutripal.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nutripal.ui.component.home.ScannerButton

@Composable
fun ScannerScreen() {
    var scannedText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ScannerButton(
            onScanResult = { result ->
                scannedText = result
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (scannedText.isNotEmpty()) scannedText else "Hasil scan akan muncul di sini.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
