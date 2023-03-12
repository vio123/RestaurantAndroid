package com.example.themeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.themeapp.view.LoginScreen
import com.example.themeapp.view.RegisterScreen
import com.example.themeapp.view.StartScreen
import com.example.themeapp.viewmodels.LoginViewModel
import com.example.themeapp.viewmodels.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun NavigationLogin(navController: NavHostController,loginViewModel: LoginViewModel,registerViewModel: RegisterViewModel,auth: FirebaseAuth){
    NavHost(navController = navController, startDestination = Screen.StartScreen.route){
        composable(route = Screen.StartScreen.route){
            StartScreen(navController)
        }
        composable(route = Screen.LoginScreen.route){
            LoginScreen(viewModel = loginViewModel,auth)
        }
        composable(route = Screen.RegisterScreen.route){
            RegisterScreen(viewModel = registerViewModel)
        }
    }
}