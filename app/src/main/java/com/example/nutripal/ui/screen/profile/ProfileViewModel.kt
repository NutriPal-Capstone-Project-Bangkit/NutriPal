package com.example.nutripal.ui.screen.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.data.remote.retrofit.ProfileApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {
    data class ProfileData(
        val name: String?,
        val profilePicture: String?
    )

    private val _profileState = MutableStateFlow<ProfileData?>(null)
    val profileState: StateFlow<ProfileData?> = _profileState

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://d817-2001-448a-2040-b8f6-45a1-836a-d0c9-a706.ngrok-free.app/") // Note the trailing slash
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val profileService = retrofit.create(ProfileApiService::class.java)

    fun fetchCurrentUserProfile() {
        val currentUser = auth.currentUser
        currentUser?.uid?.let { userId ->
            viewModelScope.launch {
                try {
                    // Call ke API
                    val response = profileService.getProfile(userId)
                    Log.d("ProfileViewModel", "Response body: ${response.body()}")

                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            val profileData = ProfileData(
                                name = body.name,
                                profilePicture = body.profilePicture
                            )
                            Log.d("ProfileViewModel", "Fetched data: $profileData")
                            _profileState.value = profileData
                        } else {
                            Log.e("ProfileViewModel", "Response body is null")
                            fetchFromFirestore(userId) // Fallback ke Firestore
                        }
                    } else {
                        Log.e("ProfileViewModel", "Response not successful: ${response.errorBody()?.string()}")
                        fetchFromFirestore(userId) // Fallback ke Firestore
                    }
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Error fetching profile", e)
                    fetchFromFirestore(userId) // Fallback ke Firestore
                }
            }
        } ?: run {
            Log.e("ProfileViewModel", "No logged-in user")
            _profileState.value = null
        }
    }

    private suspend fun fetchFromFirestore(userId: String) {
        try {
            val firestore = FirebaseFirestore.getInstance()
            val profileRef = firestore.collection("profiles").document(userId)
            val profileSnapshot = profileRef.get().await()

            if (profileSnapshot.exists()) {
                val name = profileSnapshot.getString("name")
                val profilePicture = profileSnapshot.getString("profilePicture")
                val profileData = ProfileData(name = name, profilePicture = profilePicture)
                Log.d("ProfileViewModel", "Fetched from Firestore: $profileData")
                _profileState.value = profileData
            } else {
                Log.e("ProfileViewModel", "Profile not found in Firestore")
                _profileState.value = null
            }
        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Error fetching from Firestore", e)
            _profileState.value = null
        }
    }
}
