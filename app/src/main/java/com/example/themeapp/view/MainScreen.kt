package com.example.themeapp.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.themeapp.navigation.BottomNavigationScreen
import com.example.themeapp.navigation.Navigation
import com.example.themeapp.navigation.Screen

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainScreen(navController: NavController){
    val listItems = listOf(
        Screen.Home,
        Screen.Map,
        Screen.Favorites,
        Screen.Profile,
        Screen.Settings,
    )
    Scaffold(bottomBar = {
        BottomNavigationScreen(navController = navController, items = listItems)
    }, modifier = Modifier.fillMaxSize()) {innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Navigation(navController = navController as NavHostController)
        }
    }
}