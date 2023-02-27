package com.example.themeapp.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.DesignElements.map
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.themeapp.activities.MainActivity
import com.example.themeapp.models.BestDeals
import com.example.themeapp.models.Restaurant
import com.example.themeapp.navigation.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun LadingScreen(navController: NavController,navController1: NavController){
    Surface() {
        LazyColumn(){
            item{
                val state = rememberPagerState()
                val nearbyList:MutableList<Restaurant> = mutableListOf()
                MainActivity.mainViewModel.nearbyRestaurants.value.forEachIndexed { index, restaurant ->
                    if(index < 5){
                        nearbyList.add(restaurant)
                    }
                }
                NearbyRestaurantPager(state = state, nearbyRestaurant = nearbyList)
                LaunchedEffect(key1 = state.currentPage) {
                    delay(3000)
                    var newPosition = state.currentPage + 1
                    if (newPosition > nearbyList.size - 1) newPosition = 0
                    // scrolling to the new position.
                    state.animateScrollToPage(newPosition)
                }
            }
            item{
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val hotRef = createRef()
                    val seeAllRef = createRef()
                    Text(
                        text = "Hot and New",
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.constrainAs(hotRef){
                            top.linkTo(parent.top, margin = 20.dp)
                            start.linkTo(parent.start)
                            end.linkTo(seeAllRef.start)
                            width = Dimension.fillToConstraints
                        },
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = "See all",
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.constrainAs(seeAllRef){
                            top.linkTo(parent.top, margin = 20.dp)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }.clickable {
                                    navController.navigate(Screen.AllRestaurant.route)
                        },
                        textAlign = TextAlign.End,
                    )
                }
            }
            val hotList:MutableList<Restaurant> = mutableListOf()
            MainActivity.mainViewModel.searchedRestaurant.value.forEachIndexed { index, restaurant ->
                hotList.add(restaurant)
            }
            hotList.sortWith(compareBy({it.rating},{it.reviewCount}))
            val firstHotList:MutableList<Restaurant> = mutableListOf()
            hotList.forEachIndexed { index, restaurant ->
                if(index<5)
                    firstHotList.add(restaurant)
            }
            item {
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp))
                LazyRow{
                    items(firstHotList){restaurant ->
                        RestaurantCard(restaurant = restaurant, navController = navController1)
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp))
                    }
                }
            }
            item{
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val hotRef = createRef()
                    val seeAllRef = createRef()
                    Text(
                        text = "Best deals",
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.constrainAs(hotRef){
                            top.linkTo(parent.top, margin = 20.dp)
                            start.linkTo(parent.start)
                            end.linkTo(seeAllRef.start)
                            width = Dimension.fillToConstraints
                        },
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = "See all",
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.constrainAs(seeAllRef){
                            top.linkTo(parent.top, margin = 20.dp)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        },
                        textAlign = TextAlign.End
                    )
                }
            }
            item {
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp))
                LazyRow{
                    items(MainActivity.mainViewModel.firstFiveBestDeals){ best ->
                        BestDealItem(bestDeals = best, navController = navController)
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp))
                    }
                }
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp))
            }
        }
    }
}