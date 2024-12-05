package com.example.nutripal.data.repository

import com.example.nutripal.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor() {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getUserProfile(uid: String): UserProfile {
        val document = firestore.collection("users").document(uid).get().await()
        return UserProfile(
            name = document.getString("name") ?: "",
            email = document.getString("email") ?: "",
            profilePicture = document.getString("profilePicture"),
            gender = document.getString("gender"),
            lifestyle = document.getString("lifestyle")
        )
    }
}