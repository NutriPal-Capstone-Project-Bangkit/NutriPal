package com.example.nutripal.data.remote.retrofit

import com.example.nutripal.data.remote.response.ProfileResponse
import com.example.nutripal.data.remote.response.Profile
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileApiService {

    @POST("/profile")
    fun createProfile(@Body profile: Profile): Call<ProfileResponse>

    @GET("/profile/{uid}")
    suspend fun getProfile(@Path("uid") uid: String): Response<Profile>

    @PUT("/profile/{uid}")
    fun updateProfile(
        @Path("uid") uid: String,
        @Body profile: Profile
    ): Call<ProfileResponse>
}
