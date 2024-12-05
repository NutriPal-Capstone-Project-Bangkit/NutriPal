package com.example.nutripal.data.model

data class DetectedObject(
    val box: FloatArray,
    val className: String,
    val confidence: Float
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DetectedObject

        if (!box.contentEquals(other.box)) return false
        if (className != other.className) return false
        if (confidence != other.confidence) return false

        return true
    }
}
