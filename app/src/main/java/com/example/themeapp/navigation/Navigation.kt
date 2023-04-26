package com.example.themeapp.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.themeapp.view.*

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Navigation(navController: NavHostController){
    NavHost(navController = navController, startDestination = Screen.Home.route ){
        composable(route = Screen.Home.route){
           HomeScreeen(navController = navController)
        }
        composable(
            route = Screen.DetailScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.StringType
                    defaultValue = "asdwq"
                    nullable = true
                }
            )
        ){entry->
            DetailScreen(id = entry.arguments?.getString("id").toString(), navController = navController)
        }
        composable(
            route = Screen.MenuScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.StringType
                    defaultValue = "asd"
                    nullable = true
                }
            )
        ){entry ->
            MenuScreen(id = entry.arguments?.getString("id").toString(), navController = navController)
        }
        composable(
            route = Screen.Profile.route
        ){
            ProfileScreen(navController = navController)
        }
        composable(route = Screen.Settings.route){
            SettingsScreen(navController = navController)
        }
        composable(route = Screen.Map.route){
            MapScreeen(navController = navController)
        }
        composable(route = Screen.Favorites.route){
            FavoritesScreen(navController = navController)
        }
    }
}