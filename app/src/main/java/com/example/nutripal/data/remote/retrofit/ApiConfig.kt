package com.example.nutripal.data.remote.retrofit

import com.example.nutripal.BuildConfig
import com.example.nutripal.BuildConfig.GEMINI_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    fun getBotApiService(): BotApiService {
        return createRetrofit(GEMINI_BASE_URL).create(BotApiService::class.java)
    }

    fun getNewsApiService(): NewsApiService {
        return createRetrofit(BuildConfig.NEWS_BASE_URL).create(NewsApiService::class.java)
    }

    fun getProfileApiService(): ProfileApiService {
        return createRetrofit(BuildConfig.PROFILE_BASE_URL).create(ProfileApiService::class.java)
    }

    fun getRefreshAuthTokenService(): RefreshAuthTokenService {
        return createRetrofit(BuildConfig.REFRESH_TOKEN_BASE_URL).create(RefreshAuthTokenService::class.java)
    }

    fun postVertexService(): VertexApiService {
        return createRetrofit(BuildConfig.VERTEX_BASE_URL).create(VertexApiService::class.java)
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        } else {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.NONE
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
