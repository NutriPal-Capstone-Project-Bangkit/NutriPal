package com.example.nutripal.ui.screen.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // State untuk menyimpan email dan password (password di-masking)
    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> get() = _email

    private val _password = MutableStateFlow<String>("******") // Password di-masking
    val password: StateFlow<String> get() = _password

    init {
        fetchCurrentUserData()
    }

    private fun fetchCurrentUserData() {
        viewModelScope.launch {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // Ambil email dari user yang login
                _email.value = currentUser.email ?: "Tidak tersedia"
                // Password tidak dapat diakses dari Firebase Auth, di sini hanya di-masking
            } else {
                _email.value = "Pengguna tidak ditemukan"
            }
        }
    }
}
