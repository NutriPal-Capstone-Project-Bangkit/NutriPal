package com.example.nutripal.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.compose.ui.graphics.toArgb
import com.example.nutripal.data.model.DetectedObject
import com.example.nutripal.ml.ModelNutripal3
import com.example.nutripal.ui.theme.Primary
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp

fun drawBoundingBoxes(
    context: Context,
    inputImage: Bitmap,
    boundingBoxes: List<FloatArray>,
    threshold: Float = 0.5f
): Bitmap {
    Log.d("BoundingBox", "Drawing ${boundingBoxes.size} bounding boxes")

    val mutableBitmap = inputImage.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(mutableBitmap)
    val paint = Paint().apply {
        color = Primary.toArgb()
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    boundingBoxes.forEach { box ->
        val rect = RectF(box[0], box[1], box[2], box[3])
        canvas.drawRect(rect, paint)
    }

    val textPaint = Paint().apply {
        color = Color.YELLOW
        textSize = 50f
        style = Paint.Style.FILL
    }

    val imageWidth = inputImage.width
    val imageHeight = inputImage.height

    Log.d("BoundingBox", "Image dimensions: $imageWidth x $imageHeight")

    for (boundingBox in boundingBoxes) {
        val ymin = boundingBox[0]
        val xmin = boundingBox[1]
        val ymax = boundingBox[2]
        val xmax = boundingBox[3]

        val left = (xmin * imageWidth).coerceIn(0f, imageWidth.toFloat())
        val top = (ymin * imageHeight).coerceIn(0f, imageHeight.toFloat())
        val right = (xmax * imageWidth).coerceIn(0f, imageWidth.toFloat())
        val bottom = (ymax * imageHeight).coerceIn(0f, imageHeight.toFloat())

        Log.d("BoundingBox", "Box coordinates: Left:$left, Top:$top, Right:$right, Bottom:$bottom")

        // Draw bounding box with a solid red color
        paint.color = Color.argb(255, 255, 0, 0)  // Solid red
        canvas.drawRect(left, top, right, bottom, paint)
    }

    return mutableBitmap
}

fun processImage(
    context: Context,
    imageUri: Uri,
    onResult: (Bitmap, List<DetectedObject>) -> Unit
) {
    try {
        val inputBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(320, 320, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()

        val tensorImage = TensorImage.fromBitmap(inputBitmap)
        val processedImage = imageProcessor.process(tensorImage)

        val model = ModelNutripal3.newInstance(context)
        val outputs = model.process(processedImage)
        val detectionResult = outputs.detectionResultList.get(0)

        val numberOfDetections = outputs.detectionResultList.size.toFloat()

        val detectedObjects = processDetectionResults(
            detectionResult.locationAsRectF,
            detectionResult.categoryAsString,
            detectionResult.scoreAsFloat,
            numberOfDetections,
            inputBitmap.width.toFloat(),
            inputBitmap.height.toFloat()
        )

        val resultBitmap = drawBoundingBoxes(
            context = context,
            inputImage = inputBitmap,
            boundingBoxes = detectedObjects.map { it.box }
        )

        model.close()

        onResult(resultBitmap, detectedObjects)
    } catch (e: Exception) {
        Log.e("ProcessImage", "Error processing image: ${e.message}")
        val inputBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        onResult(inputBitmap, emptyList())
    }
}

@OptIn(ExperimentalGetImage::class)
fun processImageProxy(
    imageProxy: ImageProxy,
    context: Context,
    onResult: (Bitmap, List<DetectedObject>) -> Unit
) {
    // Get bitmap from imageProxy
    val bitmap = imageProxy.toBitmap()

    val model = ModelNutripal3.newInstance(context)
    val tensorImage = TensorImage.fromBitmap(bitmap)

    val outputs = model.process(tensorImage)
    val detectionResults = outputs.detectionResultList

    val topDetection = detectionResults
        .sortedByDescending { it.scoreAsFloat }
        .firstOrNull()

    val detectedObjects = if (topDetection != null) {
        listOf(DetectedObject(
            box = floatArrayOf(
                topDetection.locationAsRectF.left,
                topDetection.locationAsRectF.top,
                topDetection.locationAsRectF.right,
                topDetection.locationAsRectF.bottom
            ),
            className = topDetection.categoryAsString,
            confidence = topDetection.scoreAsFloat
        ))
    } else {
        emptyList()
    }

    val resultBitmap = drawBoundingBoxes(context, bitmap, detectedObjects.map { it.box })
    model.close()

    onResult(resultBitmap, detectedObjects)
}

fun processDetectionResults(
    boxesBuffer: RectF,
    classesBuffer: String,
    scoresBuffer: Float,
    numberOfDetections: Float,
    imageWidth: Float,
    imageHeight: Float,
    threshold: Float = 0.5f
): List<DetectedObject> {
    val detectedObjects = mutableListOf<DetectedObject>()

    if (scoresBuffer > threshold) {
        val box = floatArrayOf(
            boxesBuffer.top / imageHeight,    // ymin
            boxesBuffer.left / imageWidth,    // xmin
            boxesBuffer.bottom / imageHeight, // ymax
            boxesBuffer.right / imageWidth    // xmax
        )

        detectedObjects.add(DetectedObject(box, classesBuffer, scoresBuffer))
    }
    return detectedObjects      
}
