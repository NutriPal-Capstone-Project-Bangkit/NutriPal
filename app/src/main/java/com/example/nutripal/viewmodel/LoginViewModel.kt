package com.example.nutripal.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.nutripal.data.auth.GoogleAuthClient
import com.example.nutripal.data.repository.AuthRepository
import com.example.nutripal.ui.navigation.Screen
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val googleAuthClient: GoogleAuthClient,
    @ApplicationContext private val context: Context
) : ViewModel() {

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

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

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
        get() = isValidEmail(email.value) && password.value.length >= 6

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    fun login(navController: NavController, rememberMe: Boolean) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = authRepository.loginUser(email.value, password.value)
                result.fold(
                    onSuccess = {
                        if (rememberMe) {
                            saveLoginSession(email.value, password.value)
                        }
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
                _isLoading.value = true
                val result = authRepository.googleSignIn(idToken)
                result.fold(
                    onSuccess = {
                        navController.navigate(Screen.PersonalDetails1.route)
                    },
                    onFailure = { e ->
                        errorMessage.value = e.message ?: "Google Sign-In gagal"
                    }
                )
            } catch (e: Exception) {
                errorMessage.value = "Terjadi kesalahan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun initializeGoogleSignIn(context: Context) {
        _googleSignInClient = googleAuthClient.getGoogleSignInClient(context)
    }

    fun getGoogleSignInIntent() = _googleSignInClient?.signInIntent

    fun saveLoginSession(email: String, password: String) {
        sharedPreferences.edit()
            .putString("email", email)
            .putString("password", password)
            .apply()
    }

    fun isUserRemembered(): Boolean {
        return sharedPreferences.contains("email") && sharedPreferences.contains("password")
    }

    fun getSavedLogin(): Pair<String, String>? {
        val savedEmail = sharedPreferences.getString("email", null)
        val savedPassword = sharedPreferences.getString("password", null)
        return if (savedEmail != null && savedPassword != null) {
            Pair(savedEmail, savedPassword)
        } else {
            null
        }
    }
}
