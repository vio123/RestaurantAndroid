package com.example.themeapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.themeapp.R
import com.example.themeapp.activities.DotsIndicator
import com.example.themeapp.activities.MainActivity
import com.example.themeapp.models.Restaurant
import com.example.themeapp.navigation.NavigationAll
import com.example.themeapp.navigation.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPermissionsApi::class)
lateinit var multiplePermissionsState: MultiplePermissionsState
@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HomeScreeen(navController: NavController){
    val context = LocalContext.current
    val navController1 = rememberNavController()
    val auth:FirebaseAuth = Firebase.auth
    val user = auth.currentUser
    Surface() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) and ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )-> {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
                val locationRequest = LocationRequest().setInterval(60).setFastestInterval(60)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                geocoder = Geocoder(context, Locale.getDefault())
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            // Things don't end here
                            // You may also update the location on your web app
                            for(location in locationResult.locations){
                                val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                                if(MainActivity.mainViewModel.city.value!=address[0].locality){
                                    MainActivity.mainViewModel.city.value = address[0].locality
                                    user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                                        if (tokenTask.isSuccessful) {
                                            val token = tokenTask.result?.token
                                            MainActivity.mainViewModel.getRestaurantList(
                                                location = MainActivity.mainViewModel.city.value,
                                                tokenUser = token.toString()
                                            )
                                        }
                                    }
                                    MainActivity.mainViewModel.getNearbyRestaurants(
                                        searchTerm = "food",
                                        location = MainActivity.mainViewModel.city.value,
                                        latitude = location.latitude,
                                        longitude = location.longitude,
                                        radius = 700
                                    )
                                    navController.navigate(Screen.Home.route)
                                }
                            }
                        }
                    },
                    Looper.myLooper()
                )
                Home(navController = navController,navController1 = navController1)
            }
            else -> {
                RequestLocationPermission(context = context, navController = navController)
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(navController: NavController,navController1:NavController){
    var textValue by remember { mutableStateOf("") }
    //NavigationAll(navController = navController as NavHostController)
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val title = createRef()
        Text(
            text = MainActivity.mainViewModel.city.value,
            Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(8.dp)
                .constrainAs(title) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.h5.fontSize,
            fontWeight = FontWeight.Bold
        )
        val column = createRef()
        Column(modifier = Modifier.constrainAs(column){
            top.linkTo(title.bottom, margin = 10.dp)
            start.linkTo(parent.start, margin = 20.dp)
            end.linkTo(parent.end, margin = 20.dp)
            bottom.linkTo(parent.bottom)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }) {
            OutlinedTextField(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                    )
                },
                placeholder = {
                    Text(
                        text = "Search for restaurants...",
                        modifier = Modifier.padding(top = 3.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                readOnly = false,
                value = textValue,
                onValueChange = {
                    textValue = it
                    MainActivity.mainViewModel.getRestaurantSearched(it)
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            )
            NavigationAll(navController = navController1 as NavHostController,navController1 = navController)
        }
    }
}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun NearbyRestaurantPager(state: PagerState,nearbyRestaurant:List<Restaurant>){
    HorizontalPager(
        state = state,
        count = nearbyRestaurant.size, modifier = Modifier
            .fillMaxHeight(0.2f)
            .fillMaxWidth()
    ) { page ->
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val image = loadPicture(
                context = LocalContext.current,
                url = nearbyRestaurant[page].img,
                defaultImg = R.drawable.ic_img
            ).value
            image?.let { img ->
                val imgRestaurant = createRef()
                Image(
                    bitmap = img.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(200.dp)
                        .constrainAs(imgRestaurant) {
                            top.linkTo(parent.top, margin = 10.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }
                )
                val resName = createRef()
                Text(
                    text = nearbyRestaurant[page].name,
                    modifier = Modifier.constrainAs(resName){
                        bottom.linkTo(imgRestaurant.bottom, margin = 10.dp)
                        start.linkTo(parent.start, margin = 10.dp)
                    },
                    color = Color.White
                )
                val dotsIndicator = createRef()
                Box(modifier = Modifier.constrainAs(dotsIndicator){
                    start.linkTo(resName.end)
                    bottom.linkTo(imgRestaurant.bottom, margin = 10.dp)
                    end.linkTo(parent.end)
                }){
                    DotsIndicator(
                        totalDots = nearbyRestaurant.size,
                        selectedIndex = state.currentPage
                    )
                }
            }
        }
    }
}
@SuppressLint("StaticFieldLeak")
private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
private lateinit var geocoder: Geocoder
@ExperimentalPermissionsApi
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun RequestLocationPermission(context: Context, navController: NavController)
{
    multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    ) { permissionStateMap ->
        if (!permissionStateMap.containsValue(false)) {
            Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.Home.route)
        } else {
            Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
            MainActivity.mainViewModel.city.value = ""
        }
    }
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Hi,nice to meet you!",
            Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(8.dp),
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.h5.fontSize,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Set your location to start exploring restaurants around you",
            Modifier
                .fillMaxWidth(0.68f)
                .height(60.dp)
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.subtitle1.fontSize,
            fontWeight = FontWeight.Medium,
            color = Color.LightGray
        )
        Button(
            onClick = {
                multiplePermissionsState.launchMultiplePermissionRequest()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(15.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background)
        ) {
            Text(text = "User current location", color = MaterialTheme.colors.surface, modifier = Modifier.padding(9.dp))
        }
    }
}