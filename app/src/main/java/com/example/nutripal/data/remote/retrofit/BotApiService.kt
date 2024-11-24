package com.example.nutripal.data.remote.retrofit

import com.example.nutripal.data.remote.response.ChatbotResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface BotApiService {
    @POST("models/gemini-1.5-flash-8b-latest:generateContent")
    @Headers("Content-Type: application/json")
    suspend fun getChatbotResponse(
        @Query("key") apiKey: String,
        @Body requestBody: ChatbotRequest
    ): ChatbotResponse
}
