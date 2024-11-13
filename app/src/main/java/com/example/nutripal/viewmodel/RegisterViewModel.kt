package com.example.nutripal.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private var username = mutableStateOf("")
    private var email = mutableStateOf("")
    private var password = mutableStateOf("")
    private var confirmPassword = mutableStateOf("")
    private var isRegisterSuccess = mutableStateOf(false)
    private var errorMessage = mutableStateOf("")
    var isChecked = mutableStateOf(false)

    private val isRegisterEnabled: Boolean
        get() = username.value.isNotEmpty() &&
                isValidEmail(email.value) &&
                password.value.isNotEmpty() &&
                password.value == confirmPassword.value &&
                isChecked.value

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    fun register() {
        viewModelScope.launch {
            if (isRegisterEnabled) {
                isRegisterSuccess.value = true
                errorMessage.value = ""
            } else {
                isRegisterSuccess.value = false
                errorMessage.value = "Please check your input fields."
            }
        }
    }
}
