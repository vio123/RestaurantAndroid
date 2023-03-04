package com.example.themeapp.activities
import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.themeapp.ui.theme.ThemeAppTheme
import com.example.themeapp.view.MainScreen
import com.example.themeapp.viewmodels.MainViewModel
import com.example.themeapp.viewmodels.RoomViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class MainActivity : ComponentActivity() {
    private val _mainViewModel: MainViewModel by viewModels()
    companion object{
        lateinit var mainViewModel:MainViewModel
        lateinit var  profilePictureUrl:String
        lateinit var email :String
        lateinit var displayName:String
    }
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = _mainViewModel
        setContent {
            val navController = rememberNavController()
            ThemeAppTheme(
                darkTheme = dark.value
            ) {
                profilePictureUrl = intent.getStringExtra("pictureUrl").toString()
                email = intent.getStringExtra("email").toString()
                displayName = intent.getStringExtra("displayName").toString()
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                   MainScreen(navController = navController)
                }
            }
        }
    }
}

val dark =mutableStateOf(false)


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ThemeAppTheme {

    }
}