package com.example.nutripal.ui.screen.personaldetails

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.nutripal.data.remote.response.Profile
import com.example.nutripal.data.remote.response.ProfileResponse
import com.example.nutripal.data.remote.retrofit.ApiConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalDetailsViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _currentPage = MutableStateFlow(0)

    // Existing fields
    val name = mutableStateOf("")
    val selectedGender = mutableStateOf("")
    val isError = mutableStateOf(false)
    val isFormValid = mutableStateOf(false)
    var profilePicture = mutableStateOf<String?>(null)
    val profilePictureUri = mutableStateOf<Uri?>(null)

    // New fields
    val age = mutableStateOf("")
    val weight = mutableStateOf("")
    val height = mutableStateOf("")
    private val activityLevel = mutableStateOf("")

    // Existing methods
    fun updateProfilePicture(uri: Uri?) {
        profilePictureUri.value = uri
        profilePicture.value = uri?.toString()
        Log.d("ProfileViewModel", "Updating profile picture URI: $uri")
    }

    fun updateProfilePictureFromString(pictureString: String?) {
        profilePicture.value = pictureString
        profilePictureUri.value = pictureString?.let { Uri.parse(it) }
    }

    fun updatePage(page: Int) {
        _currentPage.value = page
    }

    // Updated saveProfile method to include new fields
    fun saveProfile(
        name: String,
        gender: String,
        age: String,
        weight: String,
        height: String,
        activityLevel: String,
        profilePicture: String? = null
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        Log.d("ProfileSave", "Attempting to save profile with following details:")
        Log.d("ProfileSave", "UID: $uid")
        Log.d("ProfileSave", "Name: $name")
        Log.d("ProfileSave", "Gender: $gender")
        Log.d("ProfileSave", "Age: $age")
        Log.d("ProfileSave", "Weight: $weight")
        Log.d("ProfileSave", "Height: $height")
        Log.d("ProfileSave", "Activity Level: $activityLevel")

        val profile = Profile(
            uid = uid,
            name = name.trim(),
            gender = gender.trim(),
            age = age.trim(),
            weight = weight.trim(),
            height = height.trim(),
            activityLevel = activityLevel,
            profilePicture = profilePicture
        )

        val apiService = ApiConfig.getProfileApiService()
        val call = apiService.createProfile(profile)

        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                Log.d("ProfileSave", "Response received")
                if (response.isSuccessful) {
                    Log.d("ProfileSave", "Profile saved successfully: ${response.body()}")
                } else {
                    Log.e("ProfileSave", "Profile save failed. Saving to Firestore...")
                    saveProfileToFirestore(uid, profile)
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.e("ProfileSave", "Network error: ${t.message}. Saving to Firestore...")
                saveProfileToFirestore(uid, profile)
            }
        })
    }

    private fun saveProfileToFirestore(uid: String, profile: Profile) {
        val profileMap = mapOf(
            "uid" to uid,
            "name" to profile.name,
            "gender" to profile.gender,
            "age" to profile.age,
            "weight" to profile.weight,
            "height" to profile.height,
            "activityLevel" to profile.activityLevel,
            "profilePicture" to profile.profilePicture
        )

        Log.d("FirestoreSave", "Attempting to save profile map: $profileMap")

        firestore.collection("profiles").document(uid)
            .set(profileMap)
            .addOnSuccessListener {
                Log.d("FirestoreSave", "Profile saved to Firestore successfully!")
                Log.d("FirestoreSave", "Saved details: Name=${profile.name}, Age=${profile.age}, Weight=${profile.weight}, Height=${profile.height}, Activity Level=${profile.activityLevel}")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreSave", "Error saving profile to Firestore: ${e.message}", e)
                Log.e("FirestoreSave", "Failed profile map: $profileMap")
            }
    }

    // New methods for updating fields
    fun updateAge(newAge: String) {
        println("Updating age: $newAge")
        age.value = newAge
        validateForm()
    }

    fun updateWeight(newWeight: String) {
        println("Updating weight: $newWeight")
        weight.value = newWeight
        validateForm()
    }

    fun updateHeight(newHeight: String) {
        println("Updating height: $newHeight")
        height.value = newHeight
        validateForm()
    }

    fun updateActivityLevel(level: String) {
        println("Updating activity level: $level")
        if (level.isNotBlank()) {
            activityLevel.value = level
            validateForm()
        }
    }

    fun updateName(newName: String) {
        println("Updating name: $newName")
        val isValid = newName.all { it.isLetter() || it.isWhitespace() }
        name.value = newName
        isError.value = !isValid
        validateForm()
        println("Current name in ViewModel: ${name.value}")
    }

    fun updateGender(gender: String) {
        println("Updating gender: $gender")
        selectedGender.value = gender
        validateForm()
        println("Current gender in ViewModel: ${selectedGender.value}")
    }

    // Updated validateForm method to include new fields
    private fun validateForm() {
        isFormValid.value = name.value.isNotEmpty() &&
                selectedGender.value.isNotEmpty() &&
                age.value.isNotEmpty() &&
                weight.value.isNotEmpty() &&
                height.value.isNotEmpty() &&
                !isError.value
    }
}