package com.example.nutripal.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.nutripal.data.auth.GoogleAuthClient
import com.example.nutripal.data.repository.AuthRepository
import com.example.nutripal.ui.navigation.Screen
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository, private val googleAuthClient: GoogleAuthClient) : ViewModel() {
    private var _email = mutableStateOf("")
    val email = _email

    private var _password = mutableStateOf("")
    val password = _password

    private var _isLoginSuccess = mutableStateOf(false)
    val isLoginSuccess = _isLoginSuccess

    private var _errorMessage = mutableStateOf("")
    val errorMessage = _errorMessage

    private var _isAuthenticated = mutableStateOf(false)
    val isAuthenticated = _isAuthenticated

    private var _isLoading = mutableStateOf(false)
    val isLoading = _isLoading

    private var _googleSignInClient: GoogleSignInClient? = null

    init {
        // Cek status login saat ViewModel dibuat
        viewModelScope.launch {
            _isAuthenticated.value = authRepository.isUserLoggedIn()
        }
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    val isLoginEnabled: Boolean
        get() = isValidEmail(email.value) && password.value.isNotEmpty()

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    fun login(navController: NavController) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = authRepository.loginUser(email.value, password.value)
                result.fold(
                    onSuccess = {
                        // Navigasi tanpa pop-up atau stack modifikasi
                        navController.navigate(Screen.PersonalDetails1.route)
                    },
                    onFailure = { e ->
                        errorMessage.value = e.message ?: "Login gagal"
                    }
                )
            } catch (e: Exception) {
                errorMessage.value = "Terjadi kesalahan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun googleSignIn(idToken: String, navController: NavController) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val result = authRepository.googleSignIn(idToken)
                result.fold(
                    onSuccess = {
                        // Jika sign-in berhasil, arahkan ke PersonalDetailsScreen1
                        navController.navigate(Screen.PersonalDetails1.route)
                    },
                    onFailure = {
                        errorMessage.value = it.message ?: "Gagal masuk dengan Google"
                    }
                )
            } catch (e: Exception) {
                errorMessage.value = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun initializeGoogleSignIn(context: Context) {
        _googleSignInClient = googleAuthClient.getGoogleSignInClient(context)
    }

    fun getGoogleSignInIntent() = _googleSignInClient?.signInIntent
}
