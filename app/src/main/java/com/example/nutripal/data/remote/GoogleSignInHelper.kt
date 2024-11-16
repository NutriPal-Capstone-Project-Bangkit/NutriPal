package com.example.nutripal.data.remote

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

object GoogleSignInHelper {
    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        return GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1006183382175-0a1utn4g8us3uqcksqkpo0usi1e2an6k.apps.googleusercontent.com")
                .requestEmail()
                .build()
        )
    }
}
