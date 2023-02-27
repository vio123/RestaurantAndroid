package com.example.themeapp.repository

import androidx.lifecycle.LiveData
import com.example.themeapp.R
import com.example.themeapp.models.Restaurant
import com.example.themeapp.room.RestaurantDao

class RestaurantRepository(private val restaurantDao: RestaurantDao) {
    /*
    val allRestaurant:LiveData<List<Restaurant>> = restaurantDao.getAllRestaurant()
    suspend fun insert(restaurant: Restaurant){
        restaurantDao.insert(restaurant)
    }
    suspend fun delete(restaurant: Restaurant){
        restaurantDao.delete(restaurant)
    }
    suspend fun update(restaurant: Restaurant){
        restaurantDao.update(restaurant)
    }

     */
}