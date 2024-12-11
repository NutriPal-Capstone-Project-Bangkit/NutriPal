package com.example.nutripal.data.remote.retrofit

import com.example.nutripal.data.remote.response.RefreshAuthTokenResponse
import retrofit2.Response
import retrofit2.http.GET

interface RefreshAuthTokenService {
    @GET("/get-access-token")
    suspend fun getRefreshToken(): Response<RefreshAuthTokenResponse>
}
