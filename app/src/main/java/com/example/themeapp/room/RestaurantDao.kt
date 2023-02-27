package com.example.themeapp.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.themeapp.models.Restaurant

interface RestaurantDao {
    /*
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(restaurant: Restaurant)
    @Update
    suspend fun update(restaurant: Restaurant)
    @Delete
    suspend fun delete(restaurant: Restaurant)
    @Query("SELECT * FROM RestaurantTable ORDER BY id ASC")
    fun getAllRestaurant():LiveData<List<Restaurant>>
    @Query("SELECT * FROM RestaurantTable WHERE name = :name")
    fun getRestaurant(name:String):LiveData<Restaurant>

     */
}