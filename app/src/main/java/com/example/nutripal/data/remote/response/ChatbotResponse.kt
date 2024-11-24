package com.example.nutripal.data.remote.response

import com.google.gson.annotations.SerializedName

data class ChatbotResponse(

	@field:SerializedName("candidates")
	val candidates: List<CandidatesItem?>? = null,

	@field:SerializedName("modelVersion")
	val modelVersion: String? = null,

	@field:SerializedName("usageMetadata")
	val usageMetadata: UsageMetadata? = null
)

data class CandidatesItem(

	@field:SerializedName("citationMetadata")
	val citationMetadata: CitationMetadata? = null,

	@field:SerializedName("avgLogprobs")
	val avgLogprobs: Any? = null,

	@field:SerializedName("finishReason")
	val finishReason: String? = null,

	@field:SerializedName("safetyRatings")
	val safetyRatings: List<SafetyRatingsItem?>? = null,

	@field:SerializedName("content")
	val content: Content? = null
)

data class CitationMetadata(

	@field:SerializedName("citationSources")
	val citationSources: List<CitationSourcesItem?>? = null
)

data class Content(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("parts")
	val parts: List<PartsItem?>? = null
)

data class CitationSourcesItem(

	@field:SerializedName("startIndex")
	val startIndex: Int? = null,

	@field:SerializedName("endIndex")
	val endIndex: Int? = null,

	@field:SerializedName("uri")
	val uri: String? = null
)

data class SafetyRatingsItem(

	@field:SerializedName("probability")
	val probability: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)

data class UsageMetadata(

	@field:SerializedName("candidatesTokenCount")
	val candidatesTokenCount: Int? = null,

	@field:SerializedName("totalTokenCount")
	val totalTokenCount: Int? = null,

	@field:SerializedName("promptTokenCount")
	val promptTokenCount: Int? = null
)

data class PartsItem(

	@field:SerializedName("text")
	val text: String? = null
)
