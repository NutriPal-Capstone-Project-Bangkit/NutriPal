package com.example.nutripal.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.nutripal.data.repository.AuthRepository
import com.example.nutripal.ui.navigation.Screen
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
    private var isChecked = mutableStateOf(false)

    // Add loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun register(email: String, password: String, confirmPassword: String, isChecked: Boolean) {
        viewModelScope.launch {
            if (email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword && isChecked) {
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

    // Add function to reset states
    fun resetStates() {
        errorMessage.value = ""
        isRegisterSuccess.value = false
        isEmailVerified.value = false
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

    fun checkEmailVerification(navController: NavController) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val user = FirebaseAuth.getInstance().currentUser
                user?.reload()?.await()  // Reload user data to get the most updated status
                isEmailVerified.value = user?.isEmailVerified == true

                // Periksa apakah email sudah terverifikasi dan pastikan tidak ada navigasi berulang
                if (isEmailVerified.value) {
                    // Cek apakah kita sudah berada di layar Login, jika belum navigasi ke Login
                    if (navController.currentBackStackEntry?.destination?.route != Screen.Login.route) {
                        navController.navigate(Screen.Login.route) {
                            // Pop semua layar sebelumnya hingga ke Register dan pastikan hanya Login yang tetap ada
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    }
                } else {
                    // Jika email belum terverifikasi, pastikan kita menuju ke EmailVerification screen
                    if (navController.currentBackStackEntry?.destination?.route != Screen.EmailVerification.route) {
                        navController.navigate(Screen.EmailVerification.route) {
                            // Pop semua layar sebelumnya hingga ke Register dan pastikan hanya EmailVerification yang tetap ada
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    }
                }
            } catch (e: Exception) {
                errorMessage.value = "Failed to check email verification status: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun updateCheckboxState(isChecked: Boolean) {
        this.isChecked.value = isChecked
    }
}
