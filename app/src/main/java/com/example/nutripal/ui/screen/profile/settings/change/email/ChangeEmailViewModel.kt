package com.example.nutripal.ui.screen.profile.settings.change.email

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    val email = mutableStateOf("")
    val verificationMessage = mutableStateOf("")
    val isProcessing = mutableStateOf(false)

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun sendVerificationEmail(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // Basic email validation
        if (!isValidEmail(email.value)) {
            verificationMessage.value = "Format email tidak valid."
            onFailure("Invalid email format")
            return
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {
            isProcessing.value = true
            currentUser.verifyBeforeUpdateEmail(email.value)
                .addOnCompleteListener { task ->
                    isProcessing.value = false
                    if (task.isSuccessful) {
                        verificationMessage.value = "Email verifikasi telah dikirim ke ${email.value}. Silakan periksa kotak masuk Anda."
                        onSuccess()
                    } else {
                        val errorMessage = when (val exception = task.exception) {
                            is FirebaseAuthInvalidCredentialsException ->
                                "Email tidak valid. Periksa kembali alamat email Anda."
                            is FirebaseAuthUserCollisionException ->
                                "Email sudah digunakan oleh akun lain."
                            else ->
                                exception?.message ?: "Gagal mengirim email verifikasi."
                        }
                        verificationMessage.value = "Gagal: $errorMessage"
                        onFailure(errorMessage)
                    }
                }
        } else {
            verificationMessage.value = "Gagal: Pengguna tidak ditemukan. Silakan login ulang."
            onFailure("User not found")
        }
    }

    // Simple email validation function
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}