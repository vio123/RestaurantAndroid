package com.example.themeapp.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themeapp.models.Utilizator
import com.example.themeapp.network.RestaurantApi
import kotlinx.coroutines.launch

class RegisterViewModel:ViewModel() {
    val email: MutableState<String> = mutableStateOf("")
    val pass: MutableState<String> = mutableStateOf("")
    val name:MutableState<String> = mutableStateOf("")
    private val _status:MutableState<RestaurantApiStatus> = mutableStateOf(RestaurantApiStatus.LOADING)
    val status: State<RestaurantApiStatus> = _status
    fun addUser(){
        viewModelScope.launch {
            _status.value = RestaurantApiStatus.LOADING
            try {
                RestaurantApi.retrofitService.addUser(Utilizator(name = name.value, email = email.value, password = pass.value))
                _status.value = RestaurantApiStatus.DONE
            }catch (e:Exception){
                _status.value = RestaurantApiStatus.ERROR
            }
        }
    }
}