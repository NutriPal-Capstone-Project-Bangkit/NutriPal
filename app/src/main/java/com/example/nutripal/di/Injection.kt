package com.example.nutripal.di

import com.example.nutripal.data.auth.GoogleAuthClient
import com.example.nutripal.data.remote.retrofit.ApiConfig
import com.example.nutripal.data.remote.retrofit.NewsApiService
import com.example.nutripal.data.remote.retrofit.ProfileApiService
import com.example.nutripal.data.repository.AuthRepository
import com.example.nutripal.data.repository.NewsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Injection {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, retrofit: Retrofit): AuthRepository {
        val apiService = retrofit.create(ProfileApiService::class.java)
        return AuthRepository(firebaseAuth, apiService)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.PROFILE_BASE_URL) // Replace with your API's base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGoogleAuthClient(): GoogleAuthClient {
        return GoogleAuthClient()
    }

    @Provides
    @Singleton
    fun provideNewsApiService(): NewsApiService {
        return ApiConfig.getNewsApiService()
    }

    @Provides
    @Singleton
    fun provideNewsRepository(newsApiService: NewsApiService): NewsRepository {
        return NewsRepository(newsApiService)
    }
}