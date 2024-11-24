package com.example.nutripal.ui.component.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.nutripal.R
import com.example.nutripal.util.LaunchCamera

@Composable
fun ScannerButton(
    modifier: Modifier = Modifier,
    onScanResult: (String) -> Unit
) {
    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA

    // Check camera permission
    val permissionGranted = remember {
        ContextCompat.checkSelfPermission(
            context,
            cameraPermission
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            if (bitmap != null) {
                LaunchCamera().processImage(bitmap) { result ->
                    println("Recognized text: $result")
                    onScanResult(result)
                }
            } else {
                onScanResult("Gambar tidak ditemukan.")
            }
        }
    )

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                cameraLauncher.launch()
            } else {
                onScanResult("Izin kamera diperlukan.")
            }
        }
    )

    Box(
        modifier = modifier.size(150.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.Center)
                .clickable {
                    if (permissionGranted) {
                        cameraLauncher.launch()
                    } else {
                        permissionLauncher.launch(cameraPermission)
                    }
                }
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_scanner),
            contentDescription = "Scanner",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun HomeBottomNavigation(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp, top = 16.dp)
        ) {
            BottomNavigationBar(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}