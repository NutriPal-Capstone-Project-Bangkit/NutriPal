package com.example.nutripal.data.remote.retrofit

data class ChatbotRequest(
    val contents: List<Contents>
)

data class Contents(
    val parts: List<Parts>
)

data class Parts(
    val text: String
)