package com.example.themeapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.themeapp.models.Restaurant
import com.example.themeapp.repository.RestaurantRepository
import com.example.themeapp.room.RestaurantDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(application: Application): AndroidViewModel(application){
    /*
    val allRestaurants:LiveData<List<Restaurant>>
    private val repository:RestaurantRepository
    init {
        val dao = RestaurantDatabase.getDatabase(application).getRestaurantDao()
        repository = RestaurantRepository(dao)
        allRestaurants = repository.allRestaurant
    }
    fun deleteRestaurant(restaurant: Restaurant) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(restaurant)
    }
    fun updateRestaurant(restaurant: Restaurant) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(restaurant)
    }
    fun addRestaurant(restaurant: Restaurant) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(restaurant)
    }
    
     */
}