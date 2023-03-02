package com.example.themeapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.themeapp.R
import com.example.themeapp.activities.DotsIndicator
import com.example.themeapp.activities.RoundedButton
import com.example.themeapp.activities.SliderView
import com.example.themeapp.models.LoginPager
import com.example.themeapp.navigation.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@Composable
fun StartScreen(navController: NavController)
{
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        val loginImages:List<LoginPager> = listOf(
            LoginPager(
                logo = painterResource(id = R.drawable.ic_img) ,
                title ="Quick search" ,
                subTitle ="Set your location to start exploring restaurants around you"
            ),
            LoginPager(
                logo = painterResource(id = R.drawable.ic_img) ,
                title ="Quick" ,
                subTitle ="Set your location to start exploring restaurants around you"
            )
        )
        val state = rememberPagerState()
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ){
            SliderView(state, loginImages)
            Spacer(modifier = Modifier.padding(4.dp))
            Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                RoundedButton(text =  "Login") {
                    navController.navigate(Screen.LoginScreen.route)
                }
                RoundedButton(text =  "Register") {
                    navController.navigate(Screen.RegisterScreen.route)
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            DotsIndicator(
                totalDots = loginImages.size,
                selectedIndex = state.currentPage
            )
        }
        LaunchedEffect(key1 = state.currentPage) {
            delay(3000)
            var newPosition = state.currentPage + 1
            if (newPosition > loginImages.size - 1) newPosition = 0
            // scrolling to the new position.
            state.animateScrollToPage(newPosition)
        }
    }
}