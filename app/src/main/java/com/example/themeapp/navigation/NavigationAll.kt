package com.example.themeapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.themeapp.view.AllRestaurants
import com.example.themeapp.view.LadingScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationAll(navController: NavHostController,navController1: NavController){
    NavHost(navController = navController, startDestination = Screen.Landing.route ){
        composable(route = Screen.Landing.route){
            LadingScreen(navController = navController, navController1 = navController1)
        }
        composable(route = Screen.AllRestaurant.route){
            AllRestaurants(navController = navController, navController1 = navController1)
        }
    }
}