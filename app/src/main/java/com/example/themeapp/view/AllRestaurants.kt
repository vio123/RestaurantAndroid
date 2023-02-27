package com.example.themeapp.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.themeapp.activities.MainActivity.Companion.mainViewModel

@Composable
fun AllRestaurants(navController: NavController,navController1:NavController){
    Surface() {
        LazyColumn{
            items(items = mainViewModel.searchedRestaurant.value){restaurant->
                RestautantItem(restaurant = restaurant, navController = navController1)
            }
        }
    }
}