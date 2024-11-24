package com.example.nutripal.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.BuildConfig.GEMINI_API_KEY
import com.example.nutripal.data.remote.response.ChatbotResponse
import com.example.nutripal.data.remote.retrofit.ApiConfig
import com.example.nutripal.data.remote.retrofit.ChatbotRequest
import com.example.nutripal.data.remote.retrofit.Contents
import com.example.nutripal.data.remote.retrofit.Parts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    // Holds the entire chat historz
    private val _chatHistory = MutableStateFlow<List<String>>(emptyList())
    val chatHistory: StateFlow<List<String>> = _chatHistory

    private val _chatResponse = MutableStateFlow<String>("")
    val chatResponse: StateFlow<String> = _chatResponse

    fun sendMessage(message: String) {
        // Immediately add user message to chat history
        _chatHistory.value = _chatHistory.value + "User: $message"

        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getBotApiService()

                val requestBody = ChatbotRequest(
                    contents = listOf(
                        Contents(
                            parts = listOf(
                                Parts(text = message)
                            )
                        )
                    )
                )

                val response: ChatbotResponse = apiService.getChatbotResponse(GEMINI_API_KEY, requestBody)
                val botResponse = response.candidates?.firstOrNull()?.content?.parts?.joinToString("") { it?.text ?: "" }
                    ?: "Tidak ada respons dari AI."

                // Add bot response to chat history
                _chatHistory.value = _chatHistory.value + "Bot: $botResponse"

                _chatResponse.value = botResponse
            } catch (e: Exception) {
                val errorMessage = "Terjadi kesalahan: ${e.message}"
                _chatHistory.value = _chatHistory.value + "Bot: $errorMessage"
                _chatResponse.value = errorMessage
            }
        }
    }
}
