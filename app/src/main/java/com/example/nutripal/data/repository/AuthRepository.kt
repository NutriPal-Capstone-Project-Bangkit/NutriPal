package com.example.nutripal.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(private val auth: FirebaseAuth) {

    fun isUserLoggedIn(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null && currentUser.isEmailVerified
    }

    suspend fun registerUser(email: String, password: String): Result<Unit> {
        return try {
            // Registrasi pengguna baru
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
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            if (user != null && user.isEmailVerified) {
                Result.success(Unit) // Login berhasil dan email terverifikasi
            } else {
                auth.signOut()
                Result.failure(Exception("Email belum terverifikasi. Silakan verifikasi email Anda sebelum masuk."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: ""
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

    suspend fun registerUserWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
