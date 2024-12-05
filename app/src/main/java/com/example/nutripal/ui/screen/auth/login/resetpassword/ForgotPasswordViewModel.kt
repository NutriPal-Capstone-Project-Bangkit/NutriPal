package com.example.nutripal.ui.screen.auth.login.resetpassword

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class ForgotPasswordViewModel : ViewModel() {

    private val _email = mutableStateOf("")
    val email = _email

    private val _resetPasswordMessage = mutableStateOf("")
    val resetPasswordMessage = _resetPasswordMessage

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    suspend fun sendPasswordResetEmail(): Boolean {
        return try {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email.value).await()
            true
        } catch (e: Exception) {
            _resetPasswordMessage.value = "Gagal mengirim email: ${e.localizedMessage}"
            false
        }
    }

    fun checkPasswordResetStatus(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            // Dapatkan metadata user
            val metadata = currentUser.metadata
            val lastSignInTimestamp = metadata?.lastSignInTimestamp
            val currentTimestamp = System.currentTimeMillis()

            // Periksa apakah password telah diubah dalam waktu 5 menit terakhir
            return lastSignInTimestamp != null &&
                    (currentTimestamp - lastSignInTimestamp) < (5 * 60 * 1000)
        } else {
            // Tidak ada pengguna yang login
            return false
        }
    }
}
