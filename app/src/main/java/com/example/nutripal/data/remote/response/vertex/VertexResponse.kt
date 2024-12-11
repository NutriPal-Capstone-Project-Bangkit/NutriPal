package com.example.nutripal.data.remote.response.vertex

data class VertexResponse(
	val candidates: List<Candidate>,
	val usageMetadata: ResponseUsageMetadata? = null,
	val modelVersion: String? = null
)

data class Candidate(
	val content: ResponseContent,
	val finishReason: String? = null
)

data class ResponseContent(
	val role: String? = null,
	val parts: List<Part>
)

data class Part(
	val text: String? = null
)

data class ResponseUsageMetadata(
	val promptTokenCount: Int? = null,
	val candidatesTokenCount: Int? = null,
	val totalTokenCount: Int? = null
)