package com.example.nutripal.data.remote.retrofit

import com.example.nutripal.data.remote.request.StreamGenerateContentRequest
import com.example.nutripal.data.remote.response.vertex.VertexResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface VertexApiService {
    @POST("/v1/projects/nutripal-4bd4e/locations/us-central1/publishers/google/models/gemini-1.5-pro:streamGenerateContent")
    fun generateContent(
        @Header("Authorization") authHeader: String,
        @Body request: StreamGenerateContentRequest
    ): Call<List<VertexResponse>>
}
