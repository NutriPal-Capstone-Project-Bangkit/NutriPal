package com.example.nutripal.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private var email = mutableStateOf("")
    private var password = mutableStateOf("")
    private var isLoginSuccess = mutableStateOf(false)
    private var errorMessage = mutableStateOf("")

    private val isLoginEnabled: Boolean
        get() = isValidEmail(email.value) && password.value.isNotEmpty()

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    fun login() {
        viewModelScope.launch {
            if (isLoginEnabled) {
                isLoginSuccess.value = true
                errorMessage.value = ""
            } else {
                isLoginSuccess.value = false
                errorMessage.value = "Email or password is invalid."
            }
        }
    }
}
