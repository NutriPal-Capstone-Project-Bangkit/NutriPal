package com.example.nutripal.data.remote.request

import com.google.gson.annotations.SerializedName

data class StreamGenerateContentRequest(
    val contents: List<Content>,
    val systemInstruction: SystemInstruction,
    val generationConfig: GenerationConfig,
    val safetySettings: List<SafetySetting>
)

data class Content(
    val role: String,
    val parts: List<Part>
)

data class Part(
    val text: String
)

data class SystemInstruction(
    val parts: List<Part>
)

data class GenerationConfig(
    val temperature: Float,
    val maxOutputTokens: Int,
    val topP: Float
)

data class SafetySetting(
    val category: String,
    val threshold: String
)

data class VertexResponse(
    val candidates: List<Candidate>,
    val usageMetadata: UsageMetadata? = null,
    val modelVersion: String
)

data class Candidate(
    val content: Content,
    val finishReason: String? = null
)

data class UsageMetadata(
    val promptTokenCount: Int,
    val candidatesTokenCount: Int,
    val totalTokenCount: Int
)
