package com.example.nutripal.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("profile")
	val profile: Profile? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Profile(

    @field:SerializedName("uid")
	val uid: String? = null,

    @field:SerializedName("profilePicture")
	val profilePicture: String? = null,

    @field:SerializedName("gender")
	val gender: String? = null,

    @field:SerializedName("name")
	val name: String? = null,

    @field:SerializedName("activityLevel")
	val activityLevel: String? = null,

    @field:SerializedName("age")
	val age: String = null.toString(),

    @field:SerializedName("height")
	val height: String = null.toString(),

    @field:SerializedName("weight")
	val weight: String = null.toString(),
	)
