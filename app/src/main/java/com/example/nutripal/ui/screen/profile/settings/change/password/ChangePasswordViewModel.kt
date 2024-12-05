package com.example.nutripal.ui.screen.profile.settings.change.password

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    val newPassword = mutableStateOf("")
    val confirmPassword = mutableStateOf("")
    val changePasswordMessage = mutableStateOf("")

    fun updateNewPassword(password: String) {
        newPassword.value = password
    }

    fun updateConfirmPassword(password: String) {
        confirmPassword.value = password
    }

    fun changePassword(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (newPassword.value.isEmpty() || confirmPassword.value.isEmpty()) {
            changePasswordMessage.value = "Password tidak boleh kosong"
            onFailure(changePasswordMessage.value)
            return
        }

        if (newPassword.value != confirmPassword.value) {
            changePasswordMessage.value = "Password tidak cocok"
            onFailure(changePasswordMessage.value)
            return
        }

        viewModelScope.launch {
            val user = firebaseAuth.currentUser
            if (user != null) {
                user.updatePassword(newPassword.value)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            changePasswordMessage.value = "Password berhasil diperbarui"
                            onSuccess()
                        } else {
                            changePasswordMessage.value = "Gagal memperbarui password: ${task.exception?.message ?: "Kesalahan tidak diketahui"}"
                            onFailure(changePasswordMessage.value)
                        }
                    }
            } else {
                changePasswordMessage.value = "Pengguna tidak ditemukan"
                onFailure(changePasswordMessage.value)
            }
        }
    }
}