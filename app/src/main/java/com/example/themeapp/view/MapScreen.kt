package com.example.themeapp.view

import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.themeapp.R
import com.example.themeapp.activities.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.NonDisposableHandle
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.launch
import java.util.*
@SuppressLint("MissingPermission")
@Composable
fun MapScreeen(navController: NavController){
    Surface(modifier = Modifier.fillMaxSize()) {
        MainActivity.mainViewModel.getUserLocation(LocalContext.current)
        if (MainActivity.mainViewModel.userLocation.value.longitude != 0.0 && MainActivity.mainViewModel.userLocation.value.latitude != 0.0) {
            val userLocation = LatLng(
                MainActivity.mainViewModel.userLocation.value.latitude,
                MainActivity.mainViewModel.userLocation.value.longitude
            )
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(userLocation, 16.5f)
            }
            var isVisible by remember {
                mutableStateOf(false)
            }
            GoogleMap(
                cameraPositionState = cameraPositionState
            ) {
                MainActivity.mainViewModel.nearbyRestaurants.value.forEachIndexed { index, restaurant ->
                    val restaurantLocation = LatLng(
                        restaurant.coordinates.latitude,
                        restaurant.coordinates.longitude
                    )
                    val image = loadPicture(
                        context = LocalContext.current,
                        url = restaurant.img,
                        defaultImg = R.drawable.ic_img
                    ).value
                    image?.let { img ->
                        Marker(
                            position = restaurantLocation,
                            title = restaurant.name,
                            snippet = restaurant.location.display_address[0],
                            onClick = {
                                MainActivity.mainViewModel.restaurantBottomSheet.value = restaurant
                                isVisible = !isVisible
                                true
                            },
                            icon = BitmapDescriptorFactory.fromBitmap(img)
                        )
                    }
                }
            }
            AnimatedVisibility(isVisible) {
                isVisible = true
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val textRef = createRef()
                    LazyColumn(modifier = Modifier
                        .constrainAs(textRef) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }){
                        item {
                            RestautantItem(restaurant = MainActivity.mainViewModel.restaurantBottomSheet.value, navController = navController)
                        }
                    }
                }
            }
        }
    }
}