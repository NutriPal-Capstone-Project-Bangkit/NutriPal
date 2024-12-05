package com.example.nutripal.ui.screen.scan.camera

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.nutripal.R
import com.example.nutripal.ui.component.BlackStatusBar
import com.example.nutripal.ui.theme.Primary
import java.util.concurrent.Executors

@Composable
fun CameraScreen(
    navController: NavHostController,
    viewModel: CameraViewModel = viewModel()
) {
    BlackStatusBar()

    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    val previewView = remember { PreviewView(context) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var imageAnalyzer by remember { mutableStateOf<ImageAnalysis?>(null) }

    var lastAnalyzedTime by remember { mutableLongStateOf(0L) }

    val state by viewModel.state.collectAsState()

    DisposableEffect(lifecycleOwner) {
        onDispose {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.get().unbindAll()
        }
    }

    LaunchedEffect(state.isFlashOn) {
        imageCapture = ImageCapture.Builder()
            .setFlashMode(if (state.isFlashOn) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF)
            .build()

        imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { analysis ->
                analysis.setAnalyzer(
                    Executors.newSingleThreadExecutor()
                ) { imageProxy ->
                    lastAnalyzedTime = viewModel.processImageProxy(context, imageProxy, lastAnalyzedTime)
                    imageProxy.close()
                }
            }

        // Set up camera with new use cases
        imageCapture?.let { capture ->
            imageAnalyzer?.let { analyzer ->
                viewModel.setupCamera(
                    context,
                    lifecycleOwner,
                    previewView,
                    capture,
                    analyzer
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigate("home") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Close Button",
                        tint = Primary
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Scan",
                    style = MaterialTheme.typography.h6.copy(
                        color = Primary,
                        fontSize = 18.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(1f))

                // Flash Toggle Button
                IconButton(onClick = { viewModel.toggleFlash() }) {
                    Icon(
                        painter = painterResource(
                            id = if (state.isFlashOn) R.drawable.ic_flash_on
                            else R.drawable.ic_flash_off
                        ),
                        contentDescription = "Flash Toggle",
                        modifier = Modifier.size(20.dp),
                        tint = if (state.isFlashOn) Color.Yellow else Primary
                    )
                }
            }

            // Camera Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                // Combine camera preview and bounding box preview
                Box(modifier = Modifier.fillMaxSize()) {
                    // Camera preview
                    AndroidView(
                        factory = { previewView },
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                alpha = 0.2f
                            }
                    )

                    // Bounding box preview (if available) in vertical orientation
                    state.previewBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Bounding Box Preview",
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(1.5f)
                                .graphicsLayer {
                                    rotationZ = 90f
                                },
                            alpha = 0.7f
                        )
                    }

                    // Display detected objects info
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        state.detectedObjects.forEach { obj ->
                            Text(
                                text = "${obj.className}: ${
                                    String.format(
                                        "%.2f",
                                        obj.confidence * 100
                                    )
                                }%",
                                color = Color.White,
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                }
            }
        }

        // Shutter Button
        Image(
            painter = painterResource(id = R.drawable.ic_shutter),
            contentDescription = "Shutter Icon",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
                .size(70.dp)
                .clickable {
                    imageCapture?.let { capture ->
                        captureImage(capture, context) { resultUri ->
                            navController.currentBackStackEntry?.savedStateHandle?.set("captured_image_uri", resultUri)
                            navController.navigate("crop_screen")
                        }
                    }
                }
        )
    }
}

// The captureImage function remains the same as in the previous implementation
private fun captureImage(
    imageCapture: ImageCapture,
    context: Context,
    onImageCaptured: (Uri) -> Unit
) {
    val name = "NutriPal_${System.currentTimeMillis()}.jpg"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/NutriPal")
        }
    }

    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                outputFileResults.savedUri?.let { uri ->
                    onImageCaptured(uri)
                } ?: run {
                    Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    )
}
