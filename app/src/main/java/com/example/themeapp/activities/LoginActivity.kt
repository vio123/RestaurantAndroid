package com.example.themeapp.activities

import android.content.Intent
import android.icu.number.Scale
import android.os.Bundle
import android.view.Gravity.FILL
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.themeapp.R
import com.example.themeapp.models.LoginPager
import com.example.themeapp.ui.theme.ThemeAppTheme
import com.example.themeapp.view.AuthScreen
import com.example.themeapp.view.GoogleSignInButtonUi
import com.example.themeapp.viewmodels.AuthViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

class LoginActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class,
        ExperimentalFoundationApi::class, ExperimentalMaterialApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this,MainActivity::class.java)
        setContent {
            ThemeAppTheme {
                // A surface container using the 'background' color from the theme
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
                        AuthScreen(AuthViewModel())
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
        }
    }
}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun SliderView(state: PagerState, loginImages:List<LoginPager>){

    HorizontalPager(
        state = state,
        count = loginImages.size, modifier = Modifier
            .fillMaxHeight(0.5f)
            .fillMaxWidth()
    ) { page ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomCenter) {

                Image(
                    painter = loginImages[page].logo, contentDescription = "", Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxSize(0.5f), contentScale = ContentScale.Crop
                )
            }
            Text(
                text = loginImages[page].title,
                Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.h5.fontSize,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = loginImages[page].subTitle,
                Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int
) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), horizontalArrangement = Arrangement.Center
    ) {

        items(totalDots) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color = Color.DarkGray)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color = Color.LightGray)
                )
            }

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    ThemeAppTheme {
        Greeting("Android")
    }
}