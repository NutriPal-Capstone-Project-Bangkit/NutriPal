package com.example.nutripal.ui.screen.profile.edit

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.data.remote.response.Profile
import com.example.nutripal.data.remote.retrofit.ApiConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class EditProfileViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val uid: String
) : ViewModel() {

    private val _profileState = MutableStateFlow<Profile?>(null)
    val profileState: StateFlow<Profile?> = _profileState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val storage = FirebaseStorage.getInstance()

    private val profileService = ApiConfig.getProfileApiService()

    private val firestore = FirebaseFirestore.getInstance()
    private val _updateStatus = MutableStateFlow<String?>(null)
    val updateStatus: StateFlow<String?> = _updateStatus

    private val _isDataChanged = MutableStateFlow(false)
    val isDataChanged: StateFlow<Boolean> = _isDataChanged

    private val _temporaryProfilePicture = MutableStateFlow<String?>(null)
    val temporaryProfilePicture: StateFlow<String?> = _temporaryProfilePicture

    private fun setTemporaryProfilePicture(pictureUrl: String) {
        _temporaryProfilePicture.value = pictureUrl
    }

    init {
        fetchCurrentUserProfile()
    }

    fun checkIfDataChanged(
        name: String,
        gender: String,
        activityLevel: String,
        profilePicture: String,
        temporaryProfilePicture: String?,
        age: String,
        weight: String,
        height: String
    ) {
        val currentProfile = _profileState.value
        if (currentProfile != null) {
            val isChanged =
                currentProfile.name != name ||
                        currentProfile.gender != gender ||
                        currentProfile.activityLevel != activityLevel ||
                        currentProfile.profilePicture != profilePicture ||
                        temporaryProfilePicture != null || // Tambahkan kondisi ini
                        currentProfile.age != age ||
                        currentProfile.weight != weight ||
                        currentProfile.height != height
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
                                activityLevel = body.activityLevel,
                                profilePicture = body.profilePicture,
                                age = body.age,
                                weight = body.weight,
                                height = body.height
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
                        activityLevel = document.getString("activityLevel") ?: "",
                        profilePicture = document.getString("profilePicture"),
                        age = document.getString("age") ?: "",
                        weight = document.getString("weight") ?: "",
                        height = document.getString("height") ?: ""
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
    fun updateProfile(
        name: String,
        gender: String,
        activityLevel: String,
        profilePicture: String,
        age: String,
        weight: String,
        height: String
    ) {
        val currentUser = auth.currentUser
        currentUser?.uid?.let { userId ->
            val updatedProfile = mapOf(
                "name" to name,
                "gender" to gender,
                "activityLevel" to activityLevel,
                "profilePicture" to profilePicture,
                "age" to age,
                "weight" to weight,
                "height" to height
            )

            firestore.collection("profiles").document(userId)
                .update(updatedProfile)
                .addOnSuccessListener {
                    Log.d("FirestoreUpdate", "Profile updated successfully")
                    _profileState.value = Profile(
                        name = name,
                        gender = gender,
                        activityLevel = activityLevel,
                        profilePicture = profilePicture,
                        age = age,
                        weight = weight,
                        height = height
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
            val updateData = mapOf("profilePicture" to pictureUrl)

            Log.d("ProfilePictureUpdate", "Attempting to update picture URL for user $userId")
            Log.d("ProfilePictureUpdate", "New profile picture URL: $pictureUrl")

            firestore.collection("profiles").document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Document exists, proceed with update
                        firestore.collection("profiles").document(userId)
                            .update(updateData)
                            .addOnSuccessListener {
                                Log.d("ProfilePictureUpdate", "Profile picture URL updated successfully in Firestore")
                                _profileState.value = _profileState.value?.copy(profilePicture = pictureUrl)
                                _updateStatus.value = "success"
                                _temporaryProfilePicture.value = null
                            }
                            .addOnFailureListener { e ->
                                Log.e("ProfilePictureUpdateError", "Error updating profile picture URL", e)
                                _updateStatus.value = "failure"
                            }
                    } else {
                        // If document doesn't exist, set the data instead of updating
                        firestore.collection("profiles").document(userId)
                            .set(updateData)
                            .addOnSuccessListener {
                                Log.d("ProfilePictureUpdate", "Profile picture URL set in new Firestore document")
                                _profileState.value = _profileState.value?.copy(profilePicture = pictureUrl)
                                _updateStatus.value = "success"
                                _temporaryProfilePicture.value = null
                            }
                            .addOnFailureListener { e ->
                                Log.e("ProfilePictureUpdateError", "Error setting profile picture URL", e)
                                _updateStatus.value = "failure"
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ProfilePictureUpdateError", "Error checking document existence", e)
                    _updateStatus.value = "failure"
                }
        } ?: run {
            Log.e("ProfilePictureUpdateError", "No current user found")
            _updateStatus.value = "failure"
        }
    }

    fun uploadProfilePicture(imageUri: Uri) {
        viewModelScope.launch {
            try {
                // Create a reference to the profile picture in Firebase Storage
                val storageRef = storage.reference
                val profilePicRef = storageRef.child("profile_pictures/$uid.jpg")

                // Upload the image
                val uploadTask = profilePicRef.putFile(imageUri).await()

                // Get the download URL
                val downloadUrl = profilePicRef.downloadUrl.await().toString()

                // Set temporary profile picture URL (belum disimpan ke Firestore)
                setTemporaryProfilePicture(downloadUrl)
            } catch (e: Exception) {
                Log.e("ProfilePictureUpload", "Error uploading profile picture", e)
                _updateStatus.value = "failure"
            }
        }
    }
    fun saveTemporaryProfilePicture() {
        temporaryProfilePicture.value?.let { tempPictureUrl ->
            updateProfilePictureUrl(tempPictureUrl)
            // Reset temporary profile picture setelah disimpan
            _temporaryProfilePicture.value = null
        }
    }

    fun resetTemporaryProfilePicture() {
        _temporaryProfilePicture.value = null
    }

}