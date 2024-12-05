package com.example.nutripal.ui.screen.home.chatbot

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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
)

class ChatViewModel : ViewModel() {
    // Holds the entire chat history
    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory: StateFlow<List<ChatMessage>> = _chatHistory

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _chatResponse = MutableStateFlow("")
    val chatResponse: StateFlow<String> = _chatResponse

    fun sendMessage(message: String) {
        // Immediately add user message to chat history
        _chatHistory.value = _chatHistory.value + ChatMessage(
            text = message,
            isUser = true
        )

        // Set loading state to true
        _isLoading.value = true

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
                _chatHistory.value = _chatHistory.value + ChatMessage(
                    text = botResponse,
                    isUser = false
                )

                _chatResponse.value = botResponse
            } catch (e: Exception) {
                val errorMessage = "Terjadi kesalahan: ${e.message}"
                _chatHistory.value = _chatHistory.value + ChatMessage(
                    text = errorMessage,
                    isUser = false
                )
                _chatResponse.value = errorMessage
            } finally {
                // Set loading state to false
                _isLoading.value = false
            }
        }
    }
}