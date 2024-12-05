package com.example.nutripal.ui.component.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.nutripal.R

@Composable
fun ScannerButton(
    modifier: Modifier = Modifier,
    navController: NavController,
    context: Context
) {
    val cameraPermission = Manifest.permission.CAMERA
    var permissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, cameraPermission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionGranted = isGranted
            if (isGranted) {
                navController.navigate("camera_screen")
            } else {
                Toast.makeText(
                    context,
                    "Camera permission is required to scan items",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    Box(
        modifier = modifier.size(150.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clickable {
                    if (permissionGranted) {
                        navController.navigate("camera_screen")
                    } else {
                        permissionLauncher.launch(cameraPermission)
                    }
                }
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_scanner),
            contentDescription = "Scanner",
            tint = Color.Unspecified,
            modifier = Modifier.size(150.dp)
        )
    }
}

@Composable
fun HomeBottomNavigation(
    modifier: Modifier = Modifier,
    currentRoute: String,
    navController: NavController
) {
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
                modifier = Modifier.fillMaxWidth(),
                currentRoute = currentRoute,
                navController = navController
            )
        }
    }
}