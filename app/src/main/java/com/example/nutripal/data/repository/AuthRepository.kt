package com.example.nutripal.data.repository

import com.example.nutripal.data.remote.response.Profile
import com.example.nutripal.data.remote.retrofit.ProfileApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val auth: FirebaseAuth, private val apiService: ProfileApiService) {

    fun isUserLoggedIn(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null && currentUser.isEmailVerified
    }

    suspend fun registerUser(email: String, password: String): Result<Unit> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.sendEmailVerification()?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resendVerificationEmail(): Result<Unit> {
        return try {
            auth.currentUser?.let { user ->
                user.sendEmailVerification().await()
                Result.success(Unit)
            } ?: Result.failure(Exception("No user logged in"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<Unit> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user
            if (user != null && user.isEmailVerified) {
                Result.success(Unit)
            } else {
                auth.signOut()
                Result.failure(Exception("Email belum terverifikasi. Silakan verifikasi email Anda sebelum masuk."))
            }
        } catch (e: Exception) {
            // More specific error handling
            Result.failure(Exception(when {
                e.message?.contains("password is invalid", true) == true ->
                    "Email atau password yang Anda masukkan salah."
                e.message?.contains("no user record", true) == true ->
                    "Akun dengan email ini tidak ditemukan."
                e.message?.contains("malformed or has expired", true) == true ->
                    "Kredensial tidak valid. Silakan coba lagi."
                else -> e.message ?: "Login gagal"
            }))
        }
    }

    suspend fun googleSignIn(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getProfile(uid: String): Response<Profile> {
        return apiService.getProfile(uid)
    }
}
