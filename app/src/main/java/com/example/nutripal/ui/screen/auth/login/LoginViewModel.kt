package com.example.nutripal.ui.screen.auth.login

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.nutripal.data.auth.GoogleAuthClient
import com.example.nutripal.data.remote.response.Profile
import com.example.nutripal.data.repository.AuthRepository
import com.example.nutripal.ui.navigation.Screen
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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

    private var _errorMessage = mutableStateOf("")
    val errorMessage = _errorMessage

    private var _isAuthenticated = mutableStateOf(false)

    private var _isLoading = mutableStateOf(false)
    val isLoading = _isLoading

    private var _googleSignInClient: GoogleSignInClient? = null

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    init {
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
            _errorMessage.value = ""

            try {
                _isLoading.value = true
                val result = authRepository.loginUser(email.value, password.value)
                result.fold(
                    onSuccess = {
                        _errorMessage.value = ""

                        if (rememberMe) {
                            saveLoginSession(email.value, password.value)
                        }
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        currentUser?.uid?.let { uid ->
                            checkUserProfile(uid, navController)
                        } ?: run {
                            _errorMessage.value = "Gagal mendapatkan UID pengguna."
                        }
                    },
                    onFailure = { e ->
                        val errorMsg = when {
                            e.message?.contains("password is invalid", true) == true ->
                                "Email atau password yang Anda masukkan salah."
                            e.message?.contains("no user record", true) == true ->
                                "Akun dengan email ini tidak ditemukan."
                            e.message?.contains("malformed or has expired", true) == true ->
                                "Kredensial tidak valid. Silakan coba lagi."
                            else -> e.message ?: "Login gagal"
                        }
                        _errorMessage.value = errorMsg
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Terjadi kesalahan: ${e.message}"
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
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        currentUser?.uid?.let { uid ->
                            checkUserProfile(uid, navController)
                        } ?: run {
                            errorMessage.value = "Gagal mendapatkan UID pengguna."
                        }
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

    private fun saveLoginSession(email: String, password: String) {
        sharedPreferences.edit()
            .putString("email", email)
            .putString("password", password)
            .apply()
    }

    private fun isUserRemembered(): Boolean {
        return sharedPreferences.contains("email") && sharedPreferences.contains("password")
    }

    private fun getSavedLogin(): Pair<String, String>? {
        val savedEmail = sharedPreferences.getString("email", null)
        val savedPassword = sharedPreferences.getString("password", null)
        return if (savedEmail != null && savedPassword != null) {
            Pair(savedEmail, savedPassword)
        } else {
            null
        }
    }

    fun checkUserRemembered(navController: NavController) {
        viewModelScope.launch {
            if (isUserRemembered()) {
                val savedLogin = getSavedLogin()
                if (savedLogin != null) {
                    // First, try online login
                    try {
                        _isLoading.value = true
                        val result = authRepository.loginUser(savedLogin.first, savedLogin.second)
                        result.fold(
                            onSuccess = {
                                // Online login successful, proceed normally
                                checkUserProfile(FirebaseAuth.getInstance().currentUser?.uid ?: "", navController)
                            },
                            onFailure = {
                                // Online login failed, check for offline access
                                checkOfflineAccess(navController)
                            }
                        )
                    } catch (e: Exception) {
                        // Any exception during online login, fall back to offline
                        checkOfflineAccess(navController)
                    } finally {
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    private suspend fun checkOfflineAccess(navController: NavController) {
        // Check if the user has a complete profile in local storage or Firestore
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            try {
                val profileRef = FirebaseFirestore.getInstance().collection("profiles").document(uid)
                val profileSnapshot = profileRef.get().await()

                if (profileSnapshot.exists()) {
                    val profile = profileSnapshot.toObject(Profile::class.java)

                    if (profile != null &&
                        !profile.name.isNullOrEmpty() &&
                        !profile.gender.isNullOrEmpty() &&
                        !profile.activityLevel.isNullOrEmpty() &&
                        profile.height.isNotEmpty() &&
                        profile.weight.isNotEmpty() &&
                        profile.age.isNotEmpty()) {

                        // User has a complete profile, navigate to home
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        // Incomplete profile, navigate to profile completion
                        navController.navigate(Screen.PersonalDetails1.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                } else {
                    // No profile found, navigate to profile completion
                    navController.navigate(Screen.PersonalDetails1.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                // Error accessing Firestore, show error or keep on login screen
                _errorMessage.value = "Tidak dapat mengakses profil secara offline"
            }
        }
    }

    private fun checkUserProfile(uid: String, navController: NavController) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val response = authRepository.getProfile(uid)

                if (response.isSuccessful) {
                    val profile = response.body()

                    if (profile != null) {
                        if (!profile.name.isNullOrEmpty() && !profile.gender.isNullOrEmpty() && !profile.activityLevel.isNullOrEmpty() && profile.height.isNotEmpty() && profile.weight.isNotEmpty() && profile.age.isNotEmpty())  {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        } else {
                            checkProfileFromFirestore(uid, navController)
                        }
                    }
                } else {
                    checkProfileFromFirestore(uid, navController)
                }
            } catch (e: Exception) {
                // Tangani error
                errorMessage.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun checkProfileFromFirestore(uid: String, navController: NavController) {
        try {
            val profileRef = FirebaseFirestore.getInstance().collection("profiles").document(uid)
            val profileSnapshot = profileRef.get().await()

            if (profileSnapshot.exists()) {
                val profile = profileSnapshot.toObject(Profile::class.java)

                if (profile != null) {
                    if (!profile.name.isNullOrEmpty() && !profile.gender.isNullOrEmpty() && !profile.activityLevel.isNullOrEmpty() && profile.height.isNotEmpty() && profile.weight.isNotEmpty() && profile.age.isNotEmpty()) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.PersonalDetails1.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            } else {
                navController.navigate(Screen.PersonalDetails1.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        } catch (e: Exception) {
            errorMessage.value = "An error occurred while fetching data from Firestore: ${e.message}"
        }
    }
}
