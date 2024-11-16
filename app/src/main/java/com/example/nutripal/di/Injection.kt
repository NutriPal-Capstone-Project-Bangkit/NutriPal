package com.example.nutripal.di

import com.example.nutripal.data.auth.GoogleAuthClient
import com.example.nutripal.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepository(firebaseAuth)
    }
    @Provides
    @Singleton
    fun provideGoogleAuthClient(): GoogleAuthClient {
        return GoogleAuthClient()
    }
}