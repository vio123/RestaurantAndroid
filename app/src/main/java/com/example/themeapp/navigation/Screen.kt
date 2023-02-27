package com.example.themeapp.navigation

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class Screen(val title:String="",val icon:ImageVector=Icons.Default.Close,val route:String){
    object DetailScreen : Screen(route = "detail_screen")
    object Home : Screen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object AllRestaurant : Screen(
        route = "allRestaurant"
    )
    object Map : Screen(
        route = "map",
        title = "Map",
        icon = Icons.Default.LocationOn
    )
    object Favorites : Screen(
        route = "favorites",
        title = "Favorites",
        icon = Icons.Default.Favorite
    )
    object Profile : Screen(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )

    object Settings : Screen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
    object Landing : Screen(
        route = "landing"
    )
    fun withArgs(vararg args:String):String{
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}

@Composable
fun BottomNavigationScreen(navController: NavController, items: List<Screen>){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BottomNavigation(backgroundColor = MaterialTheme.colors.surface) {
        items.forEach { screens->
             BottomNavigationItem(
                 selected = currentDestination?.route == screens.route,
                 onClick = {
                     navController.navigate(screens.route){
                         launchSingleTop = true
                         popUpTo(navController.graph.findStartDestination().id ){
                             saveState = true
                         }
                         restoreState = true
                     }
                 },
                 icon = {Icon(imageVector = screens.icon , contentDescription = "")},
                 alwaysShowLabel = false
             )
        }
    }
}