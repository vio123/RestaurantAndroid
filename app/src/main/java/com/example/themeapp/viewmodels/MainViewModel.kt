package com.example.themeapp.viewmodels
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.DesignElements.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themeapp.SearchWidgetState
import com.example.themeapp.activities.MainActivity
import com.example.themeapp.models.*
import com.example.themeapp.network.RestaurantApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

enum class RestaurantApiStatus {LOADING, ERROR, DONE}
class MainViewModel : ViewModel() {
    val restaurantBottomSheet:MutableState<Restaurant> = mutableStateOf(Restaurant())
    private val _userLocation:MutableState<Coordinates> = mutableStateOf(Coordinates(0.0,0.0))
    val userLocation:State<Coordinates> = _userLocation
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private val _firstFiveBestDeals:MutableList<BestDeals> = mutableListOf()
    val firstFiveBestDeals:List<BestDeals> = _firstFiveBestDeals
    private val _nearbyRestaurants:MutableState<List<Restaurant>> = mutableStateOf(listOf())
    val nearbyRestaurants:State<List<Restaurant>> = _nearbyRestaurants
    val city:MutableState<String> = mutableStateOf("")
    private val _bestDeals:MutableState<List<BestDeals>> = mutableStateOf(listOf())
    val bestDeals:State<List<BestDeals>> = _bestDeals
    private val _map:MutableMap<String,Int> = HashMap()
    private lateinit var  sortedMap:Map<String,Int>
    private  val mapImg:MutableMap<String,String> = HashMap()
    private val _statusMap:MutableState<RestaurantApiStatus> = mutableStateOf(RestaurantApiStatus.LOADING)
    val statusMap:State<RestaurantApiStatus> = _statusMap
    private val _reviews = mutableStateListOf<Review>()
    val reviews get() = _reviews
    private val _user:MutableState<User> = mutableStateOf(User())
    val user:State<User> = _user
    private  val _selected:MutableState<String> = mutableStateOf("")
     val selected:State<String> = _selected
    private val token:String ="Bearer 9DTzSDNr_hontOlr8HypqkIhdJcZcGpxPTqHS5v-TgbQ2Z_qW-LdxjsKwnnIOOd8Sks7ttMj9n8AZ_fUtas8VCGrnZ9h3mPF50FgFYm9drVQZcuDBXOCAEQybQzQYnYx"
    // TODO: Create properties to represent MutableLiveData and LiveData for the API status
    private val _status:MutableState<RestaurantApiStatus> = mutableStateOf(RestaurantApiStatus.LOADING)
    val status:State<RestaurantApiStatus> = _status
    // TODO: Create properties to represent MutableLiveData and LiveData for a list of amphibian objects
    private val _restaurants:MutableState<List<Restaurant>> = mutableStateOf(listOf())

    private val _searchedRestaurant:MutableState<List<Restaurant>> = mutableStateOf(listOf())
    val searchedRestaurant:State<List<Restaurant>> = _searchedRestaurant
    // TODO: Create properties to represent MutableLiveData and LiveData for a single amphibian object.
    //  This will be used to display the details of an amphibian when a list item is clicked
    private val _restaurant : MutableState<RestaurantDetails> = mutableStateOf(RestaurantDetails(id = "", name = "",img="", rating = 0.0, location = Location(display_address = emptyList()),isClaimed=false, isOpen =false,phone="",reviewCount=0,categories= emptyList(),coordinates= Coordinates(0.0,0.0),photos= emptyList(), open = emptyList(), price = ""))
    val restaurant: State<RestaurantDetails> = _restaurant

    val  dialogState:MutableState<Boolean> = mutableStateOf(value = false)
    val showDialog:MutableState<Boolean> = mutableStateOf(false)

    fun getNearbyRestaurants(searchTerm: String,location: String,latitude:Double,longitude:Double,radius:Int){
        viewModelScope.launch {
            _status.value = RestaurantApiStatus.LOADING
            try {
                _nearbyRestaurants.value = RestaurantApi.retrofitService.getNearbyRestaurants(
                    token = token,
                    searchTerm = searchTerm,
                    location = location,
                    latitude = latitude,
                    longitude = longitude,
                    radius = radius
                ).restaurant
                _status.value = RestaurantApiStatus.DONE
            }catch (e:Exception){
                _status.value = RestaurantApiStatus.ERROR
                _restaurants.value = emptyList()
            }
        }
    }
    @SuppressLint("MissingPermission")
    fun getUserLocation(context: Context){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            context)
        geocoder = Geocoder(context, Locale.getDefault())
        val lastLocation = fusedLocationProviderClient.lastLocation
        lastLocation.addOnSuccessListener {
            _userLocation.value = Coordinates(latitude = it.latitude, longitude = it.longitude)
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun sortBestDeals(){
        _map.clear()
        mapImg.clear()
        _firstFiveBestDeals.clear()
        _searchedRestaurant.value.forEachIndexed { index, restaurant ->
            restaurant.categories.forEachIndexed { index, category ->
                _map[category.title] = _map.getOrDefault(category.title,0) + 1
                mapImg[category.title] = restaurant.img
            }
        }
        val bestDealsList:MutableList<BestDeals> = mutableListOf()
        sortedMap = _map.toList().sortedByDescending { (k, v) -> v }.toMap()
        sortedMap.forEach { key, value->
            bestDealsList.add(BestDeals(img = mapImg[key].toString(), category = key, nrPlaces = sortedMap[key]!!))
        }
        _bestDeals.value = bestDealsList
        _bestDeals.value.forEachIndexed { index, bestDeals ->
            if(index < 5){
                _firstFiveBestDeals.add(bestDeals)
            }
        }
    }

   @RequiresApi(Build.VERSION_CODES.N)
   fun getRestaurantList( location:String,tokenUser:String) {
        viewModelScope.launch {
            _status.value = RestaurantApiStatus.LOADING

            try {
                _selected.value = "best_match"
                _restaurants.value =RestaurantApi.retrofitService.getRestaurants(token=tokenUser, location = location).restaurant
                _status.value = RestaurantApiStatus.DONE
                _searchedRestaurant.value = _restaurants.value
                sortBestDeals()
            } catch (e1: Exception) {
                _status.value = RestaurantApiStatus.ERROR
                _restaurants.value = emptyList()
                Log.e("test123",e1.toString())
            }
        }

    }
    fun getRestaurantDetail(id:String,tokenUser: String){
        viewModelScope.launch {
            _statusMap.value = RestaurantApiStatus.LOADING
            try {
                _restaurant.value = RestaurantApi.retrofitService.getRestaurantDetail(token= tokenUser,searchById = id)
                _statusMap.value = RestaurantApiStatus.DONE
                Log.e("test123","details")
            }catch (e1:Exception){
                _statusMap.value = RestaurantApiStatus.ERROR
                Log.e("details",e1.toString())
            }
        }
    }
    private fun getRestaurantSorted(searchTerm:String, location:String, limit:Int=20,sortBy:String){
        var lim:Int = limit
        if(limit<0||limit>50){
            lim = 20
        }
        viewModelScope.launch {
            _status.value = RestaurantApiStatus.LOADING
            try{
                _restaurants.value = RestaurantApi.retrofitService.getRestaurantsByFilter(token=token, searchTerm = searchTerm, location = location,limit=lim, sortBy = sortBy).restaurant
                _status.value = RestaurantApiStatus.DONE
                _searchedRestaurant.value = _restaurants.value
            }catch (e1:Exception){
                _status.value = RestaurantApiStatus.ERROR
                _restaurants.value = emptyList()
                Log.e("test123", e1.toString())
            }
        }
    }
    fun getRestaurantSearched(name:String){
            _searchedRestaurant.value = _restaurants.value.filter {
                it.name.lowercase().contains(name)
            }
    }
    fun selectedFilter(type:String,searchTerm: String,location: String,limit: Int=20){
        _selected.value = type
        getRestaurantSorted(searchTerm = searchTerm,location = location,limit = limit, sortBy = type)
    }
    fun getReviews(id:String,tokenUser: String){
        viewModelScope.launch{
            _status.value = RestaurantApiStatus.LOADING
            try{
                _status.value = RestaurantApiStatus.DONE
                _reviews.clear()
                 _reviews.addAll(RestaurantApi.retrofitService.getRestaurantReviews(token = tokenUser, id = id).reviews)
            }catch (e1:Exception){
                Log.e("test123",e1.toString())
            }
        }
    }
    fun addReview(review: Review,tokenUser: String){
        viewModelScope.launch{
            _status.value = RestaurantApiStatus.LOADING
            try{
                _status.value = RestaurantApiStatus.DONE
                RestaurantApi.retrofitService.addReview(requestBody = review, token = tokenUser)
                _reviews.add(review)
            }catch (e1:Exception){
                Log.e("test123",e1.toString())
            }
        }
    }
    fun getUser(id:String,tokenUser: String){
        viewModelScope.launch{
            _status.value = RestaurantApiStatus.LOADING
            try{
                _status.value = RestaurantApiStatus.DONE
                _user.value = RestaurantApi.retrofitService.getUser(token = tokenUser, idUser = id)
            }catch (e1:Exception){
                Log.e("test123",e1.toString())
            }
        }
    }
}