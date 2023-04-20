package com.example.themeapp.viewmodels

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themeapp.models.*
import com.example.themeapp.network.RestaurantApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsRestaurantViewModel : ViewModel() {
    private val _restaurant : MutableState<RestaurantDetails> = mutableStateOf(RestaurantDetails(id = "", name = "",img="", rating = 0.0, location = Location(display_address = emptyList()),isClaimed=false, isOpen =false,phone="",reviewCount=0,categories= emptyList(),coordinates= Coordinates(0.0,0.0),photos= emptyList(), open = emptyList(), price = ""))
    val restaurant = derivedStateOf {
        _restaurant.value
    }
    private val _reviews = mutableStateListOf<Review>()
    val reviews = derivedStateOf {
        _reviews
    }
    private val _user:MutableState<User> = mutableStateOf(User())
    val user = derivedStateOf {
        _user.value
    }
    private val _statusMap:MutableState<RestaurantApiStatus> = mutableStateOf(RestaurantApiStatus.LOADING)
    val statusMap: State<RestaurantApiStatus> = _statusMap
    val showDialog:MutableState<Boolean> = mutableStateOf(false)
    fun getRestaurantDetail(id: String, tokenUser: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _statusMap.value = RestaurantApiStatus.LOADING
            try {
                _restaurant.value = RestaurantApi.retrofitService.getRestaurantDetail(token= tokenUser,searchById = id)
                _statusMap.value = RestaurantApiStatus.DONE
                Log.e("details",_restaurant.value.rating.toString())
            }catch (e1:Exception){
                _statusMap.value = RestaurantApiStatus.ERROR
                Log.e("details",e1.toString())
            }
        }
    }
    fun getReviews(id:String,tokenUser: String){
        viewModelScope.launch{
            _statusMap.value = RestaurantApiStatus.LOADING
            try{
                _statusMap.value = RestaurantApiStatus.DONE
                _reviews.clear()
                _reviews.addAll(RestaurantApi.retrofitService.getRestaurantReviews(token = tokenUser, id = id).reviews)
            }catch (e1:Exception){
                Log.e("test123",e1.toString())
            }
        }
    }
    fun getUser(id:String,tokenUser: String){
        viewModelScope.launch{
            _statusMap.value = RestaurantApiStatus.LOADING
            try{
                _statusMap.value = RestaurantApiStatus.DONE
                _user.value = RestaurantApi.retrofitService.getUser(token = tokenUser, idUser = id)
            }catch (e1:Exception){
                Log.e("test123",e1.toString())
            }
        }
    }
    fun addReview(review: Review,tokenUser: String){
        viewModelScope.launch{
            _statusMap.value = RestaurantApiStatus.LOADING
            try{
                _statusMap.value = RestaurantApiStatus.DONE
                RestaurantApi.retrofitService.addReview(requestBody = review, token = tokenUser)
            }catch (e1:Exception){
                Log.e("test123",e1.toString())
            }
        }
    }
}