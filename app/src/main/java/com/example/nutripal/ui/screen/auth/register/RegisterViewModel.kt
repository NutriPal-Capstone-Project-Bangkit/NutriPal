package com.example.nutripal.ui.screen.auth.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    var isRegisterSuccess = mutableStateOf(false)
    var errorMessage = mutableStateOf("")
    var isEmailVerified = mutableStateOf(false)
    var showVerificationDialog = mutableStateOf(false)


    // Add loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            if (email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
                try {
                    _isLoading.value = true
                    errorMessage.value = ""
                    isRegisterSuccess.value = false

                    val result = authRepository.registerUser(email, password)
                    result.fold(
                        onSuccess = {
                            isRegisterSuccess.value = true // Sukses registrasi
                            errorMessage.value = ""
                        },
                        onFailure = { e ->
                            isRegisterSuccess.value = false
                            errorMessage.value = e.message ?: "Terjadi kesalahan"
                        }
                    )
                } catch (e: Exception) {
                    isRegisterSuccess.value = false
                    errorMessage.value = "Terjadi kesalahan: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            } else {
                isRegisterSuccess.value = false
                errorMessage.value = "Mohon lengkapi semua field"
            }
        }
    }

    fun resendVerificationEmail() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = authRepository.resendVerificationEmail()
                if (result.isSuccess) {
                    errorMessage.value = "Verification email resent successfully"
                } else {
                    errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to resend verification email"
                }
            } catch (e: Exception) {
                errorMessage.value = "Failed to resend verification email: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun checkEmailVerification() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val user = FirebaseAuth.getInstance().currentUser
                user?.reload()?.await()
                isEmailVerified.value = user?.isEmailVerified == true

                if (isEmailVerified.value) {
                    showVerificationDialog.value = true
                }
            } catch (e: Exception) {
                errorMessage.value = "Failed to check email verification status: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
