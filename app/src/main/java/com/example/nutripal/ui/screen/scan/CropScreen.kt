@file:Suppress("DEPRECATION")

package com.example.nutripal.ui.screen.scan

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

@Composable
fun CropScreen(
    navController: NavHostController,
) {

    // Retrieve the captured image URI from the previous screen's saved state
    val capturedImageUri = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Uri>("captured_image_uri")

    var croppedImageUri by remember { mutableStateOf<Uri?>(null) }
    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val croppedImageUri = result.uriContent
            croppedImageUri?.let { uri ->
                // Save the cropped image URI to the next screen's saved state
                navController.previousBackStackEntry?.savedStateHandle?.set("cropped_image_uri", uri)

                // Navigate directly to ResultScreen
                navController.navigate("result_screen") {
                    // Configure navigation to avoid going back to CropScreen
                    popUpTo("crop_screen") { inclusive = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }

    // Automatically launch crop when the screen is first composed with a captured image
    LaunchedEffect(capturedImageUri) {
        capturedImageUri?.let { uri ->
            cropImageLauncher.launch(
                CropImageContractOptions(
                    uri = uri,
                    cropImageOptions = CropImageOptions(
                        guidelines = CropImageView.Guidelines.ON,
                        fixAspectRatio = true,
                        aspectRatioX = 1,
                        aspectRatioY = 1
                    )
                )
            )
        }
    }
}