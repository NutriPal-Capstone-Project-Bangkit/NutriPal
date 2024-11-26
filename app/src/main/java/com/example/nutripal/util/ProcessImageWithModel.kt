//package com.example.nutripal.util
//
//import android.content.Context
//import android.graphics.Bitmap
//import com.example.nutripal.ml.NutripalModel
//import com.google.mlkit.vision.text.TextRecognizer
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
//import java.nio.ByteBuffer
//
//fun processImageWithModel(
//    context: Context,
//    bitmap: Bitmap,
//    onResult: (String) -> Unit
//) {
//    // Konversi Bitmap ke ByteBuffer untuk input model
//    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 320, 320, true)
//    val byteBuffer = ByteBuffer.allocateDirect(320 * 320 * 3 * 4) // Float32
//    resizedBitmap.copyPixelsToBuffer(byteBuffer)
//    byteBuffer.rewind()
//
//    // Load model dan lakukan inferensi
//    val model = NutripalModel.newInstance(context)
//    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 320, 320, 3), DataType.FLOAT32)
//    inputFeature0.loadBuffer(byteBuffer)
//
//    val outputs = model.process(inputFeature0)
//    val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//
//    // Interpretasi hasil prediksi
//    val isNutritionFacts = interpretModelOutput(outputFeature0.floatArray)
//    model.close()
//
//    if (isNutritionFacts) {
//        // Gunakan OCR untuk mengekstrak teks dari gambar
//        val ocrProcessor = TextRecognizer.Builder(context).build()
//        val frame = Frame.Builder().setBitmap(bitmap).build()
//        val text = ocrProcessor.detect(frame)?.text ?: "Tidak ada teks terdeteksi"
//        onResult(text)
//    } else {
//        onResult("Bukan Nutrition Facts.")
//    }
//}
//
//fun interpretModelOutput(output: FloatArray): Boolean {
//    return output.maxOrNull() ?: 0f > 0.5f
//}
