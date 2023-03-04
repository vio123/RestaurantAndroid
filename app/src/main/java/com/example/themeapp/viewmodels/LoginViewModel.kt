package com.example.themeapp.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    val email:MutableState<String> = mutableStateOf("")
    val pass:MutableState<String> = mutableStateOf("")
}