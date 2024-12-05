package com.example.nutripal.data.model

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val profilePicture: String? = null,
    val gender: String? = null,
    val lifestyle: String? = null
)