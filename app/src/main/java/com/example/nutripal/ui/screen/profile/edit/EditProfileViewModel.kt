package com.example.nutripal.ui.screen.profile.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.data.remote.response.Profile
import com.example.nutripal.data.remote.retrofit.ProfileApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditProfileViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val apiService: ProfileApiService,
    private val uid: String
) : ViewModel() {

    private val _profileState = MutableStateFlow<Profile?>(null)
    val profileState: StateFlow<Profile?> = _profileState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val storage = FirebaseStorage.getInstance()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://d817-2001-448a-2040-b8f6-45a1-836a-d0c9-a706.ngrok-free.app/") // Base URL for the API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val profileService = retrofit.create(ProfileApiService::class.java)
    private val firestore = FirebaseFirestore.getInstance()
    private val _updateStatus = MutableStateFlow<String?>(null)
    val updateStatus: StateFlow<String?> = _updateStatus

    private val _isDataChanged = MutableStateFlow(false)
    val isDataChanged: StateFlow<Boolean> = _isDataChanged

    init {
        fetchCurrentUserProfile()
    }

    fun checkIfDataChanged(
        name: String,
        gender: String,
        lifestyle: String,
        profilePicture: String
    ) {
        val currentProfile = _profileState.value
        if (currentProfile != null) {
            val isChanged =
                currentProfile.name != name || currentProfile.gender != gender || currentProfile.lifestyle != lifestyle || currentProfile.profilePicture != profilePicture
            _isDataChanged.value = isChanged
        }
    }

    // Fetch the current user profile
    fun fetchCurrentUserProfile() {
        val currentUser = auth.currentUser
        currentUser?.uid?.let { userId ->
            viewModelScope.launch {
                try {
                    val response = profileService.getProfile(userId)
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            _profileState.value = Profile(
                                name = body.name,
                                gender = body.gender,
                                lifestyle = body.lifestyle,
                                profilePicture = body.profilePicture
                            )
                        }
                    } else {
                        Log.e("ProfileFetchError", "API call failed with code: ${response.code()}")
                        fetchProfileFromFirestore(userId)
                    }
                } catch (e: Exception) {
                    Log.e("ProfileFetchError", "Error fetching profile from API", e)
                    fetchProfileFromFirestore(userId)
                }
            }
        }
    }

    // Fallback to fetch profile from Firestore
    private fun fetchProfileFromFirestore(userId: String) {
        firestore.collection("profiles").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val profile = Profile(
                        name = document.getString("name") ?: "",
                        gender = document.getString("gender") ?: "",
                        lifestyle = document.getString("lifestyle") ?: "",
                        profilePicture = document.getString("profilePicture")
                    )
                    _profileState.value = profile
                    Log.d("FirestoreFetch", "Fetched profile from Firestore: $profile")
                } else {
                    Log.e("FirestoreFetchError", "No such document for user: $userId")
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreFetchError", "Error fetching profile from Firestore", e)
            }
    }

    // Update profile fields in Firestore
    fun updateProfile(name: String, gender: String, lifestyle: String, profilePicture: String) {
        val currentUser = auth.currentUser
        currentUser?.uid?.let { userId ->
            val updatedProfile = mapOf(
                "name" to name,
                "gender" to gender,
                "lifestyle" to lifestyle,
                "profilePicture" to profilePicture
            )

            firestore.collection("profiles").document(userId)
                .update(updatedProfile)
                .addOnSuccessListener {
                    Log.d("FirestoreUpdate", "Profile updated successfully")
                    _profileState.value = Profile(
                        name = name,
                        gender = gender,
                        lifestyle = lifestyle,
                        profilePicture = profilePicture
                    )
                    _updateStatus.value = "success"

                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreUpdateError", "Error updating profile", e)
                    _updateStatus.value = "failure"
                }
        }
    }

    fun resetUpdateStatus() {
        _updateStatus.value = null
    }

    fun updateProfilePictureUrl(pictureUrl: String) {
        val currentUser = auth.currentUser
        currentUser?.uid?.let { userId ->
            // Directly update the profile picture URL in Firestore
            val updateData = mapOf("profilePicture" to pictureUrl)

            firestore.collection("profiles").document(userId)
                .update(updateData)
                .addOnSuccessListener {
                    Log.d("ProfilePictureUpdate", "Profile picture URL updated successfully")

                    // Update the current profile state
                    _profileState.value = _profileState.value?.copy(profilePicture = pictureUrl)

                    _updateStatus.value = "success"
                }
                .addOnFailureListener { e ->
                    Log.e("ProfilePictureUpdateError", "Error updating profile picture URL", e)
                    _updateStatus.value = "failure"
                }
        }
    }
}