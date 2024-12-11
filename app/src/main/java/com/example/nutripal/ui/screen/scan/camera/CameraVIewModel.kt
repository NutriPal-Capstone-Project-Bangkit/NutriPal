package com.example.nutripal.ui.screen.scan.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.data.model.DetectedObject
import com.example.nutripal.util.processImage
import com.example.nutripal.util.processImageProxy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CameraScreenState(
    val imageUri: Uri? = null,
    val previewBitmap: Bitmap? = null,
    val detectedObjects: List<DetectedObject> = emptyList(),
    val isFlashOn: Boolean = false,
    val boundingBox: Rect? = null
)

class CameraViewModel : ViewModel() {
    private val _state = MutableStateFlow(CameraScreenState())
    val state = _state.asStateFlow()

    fun setupCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        imageCapture: ImageCapture,
        imageAnalyzer: ImageAnalysis
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val cameraProvider = cameraProviderFuture.get()

        // Unbind previous camera use cases
        cameraProvider.unbindAll()

        // Setup camera selector
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        // Create Preview use case
        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }

        // Bind use cases to camera
        val camera = cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageAnalyzer,
            imageCapture
        )

        // Enable/disable torch based on flash state
        camera.cameraControl.enableTorch(_state.value.isFlashOn)
    }

    fun toggleFlash() {
        _state.update { currentState ->
            currentState.copy(isFlashOn = !currentState.isFlashOn)
        }
    }

    private fun updatePreviewBitmap(bitmap: Bitmap?, detectedObjects: List<DetectedObject>) {
        _state.update { currentState ->
            currentState.copy(
                previewBitmap = bitmap,
                detectedObjects = detectedObjects
            )
        }
    }

    fun processImageProxy(
        context: Context,
        imageProxy: androidx.camera.core.ImageProxy,
        lastAnalyzedTime: Long
    ): Long {
        var updatedLastAnalyzedTime = lastAnalyzedTime
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAnalyzedTime >= 100) { // Update every 100ms
            updatedLastAnalyzedTime = currentTime
            processImageProxy(imageProxy, context) { resultBitmap, objects ->
                updatePreviewBitmap(resultBitmap, objects)
            }
        }
        return updatedLastAnalyzedTime
    }
}