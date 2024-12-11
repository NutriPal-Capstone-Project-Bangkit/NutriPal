package com.example.nutripal.data.remote.response

import com.google.gson.annotations.SerializedName

data class RefreshAuthTokenResponse(
	@field:SerializedName("access_token")
	val accessToken: AccessToken? = null
)

data class AccessToken(
	@field:SerializedName("token")
	val token: String? = null
)
