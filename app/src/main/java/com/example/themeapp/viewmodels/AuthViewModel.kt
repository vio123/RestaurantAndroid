package com.example.themeapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.themeapp.login.getGoogleSignInClient
import com.example.themeapp.models.GoogleUser
import com.example.themeapp.models.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel(){
    private val _user: MutableStateFlow<GoogleUser?> = MutableStateFlow(null)
    val user: StateFlow<GoogleUser?> = _user

    suspend fun signIn(email: String, displayName: String,profilePicture:String){
        delay(2000)//simulating network call
        _user.value = GoogleUser(email, displayName,profilePicture)
    }
}