package com.example.nutripal.di

import android.app.Application
import android.content.Context
import com.example.nutripal.BuildConfig
import com.example.nutripal.data.auth.GoogleAuthClient
import com.example.nutripal.data.local.NutritionDatabase
import com.example.nutripal.data.local.dao.HistoryDao
import com.example.nutripal.data.remote.retrofit.ApiConfig
import com.example.nutripal.data.remote.retrofit.NewsApiService
import com.example.nutripal.data.remote.retrofit.ProfileApiService
import com.example.nutripal.data.remote.retrofit.RefreshAuthTokenService
import com.example.nutripal.data.repository.AuthRepository
import com.example.nutripal.data.repository.NewsRepository
import com.example.nutripal.data.repository.OCRRepository
import com.example.nutripal.ui.screen.scan.result.savetodaily.AddToDailyViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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

    @Provides
    fun provideAddToDailyViewModel(
        refreshAuthTokenService: RefreshAuthTokenService,
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): AddToDailyViewModel {
        return AddToDailyViewModel(refreshAuthTokenService, firestore, auth)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideNutritionDatabase(context: Context): NutritionDatabase {
        return NutritionDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideHistoryDao(database: NutritionDatabase): HistoryDao {
        return database.historyDao()
    }

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
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
            .baseUrl(BuildConfig.PROFILE_BASE_URL)
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

    @Provides
    fun provideRefreshAuthTokenService(): RefreshAuthTokenService =
        ApiConfig.getRefreshAuthTokenService()

    @Provides
    fun provideOCRRepository(): OCRRepository {
        return OCRRepository()
    }

}
