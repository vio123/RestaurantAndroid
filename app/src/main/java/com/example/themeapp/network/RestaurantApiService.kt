package com.example.themeapp.network

import com.example.themeapp.models.Restaurant
import com.example.themeapp.models.RestaurantDetails
import com.example.themeapp.models.Restaurants
import com.example.themeapp.models.Reviews
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL="https://api.yelp.com/v3/"

// TODO: Build the Moshi object with Kotlin adapter factory that Retrofit will be using to parse JSON
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
// TODO: Build a Retrofit object with the Moshi converter
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface RestaurantApiService {
    // TODO: Declare a suspended function to get the list of restaurants
    @GET("businesses/search")
    suspend fun getRestaurants(
        @Header("Authorization") token:String,
        @Query("term") searchTerm:String,
        @Query("location") location:String,
        @Query("limit") limit:Int,
        @Query("attributes") attributes:String
    ): Restaurants
    @GET("businesses/search")
    suspend fun getRestaurantsByFilter(
        @Header("Authorization") token:String,
        @Query("term") searchTerm:String,
        @Query("location") location: String,
        @Query("limit") limit:Int,
        @Query("sort_by") sortBy:String
    ):Restaurants
    @GET("businesses/{id}")
    suspend fun getRestaurantDetail(
        @Header("Authorization") token:String,
        @Path("id") searchById:String
    ):RestaurantDetails
    @GET("businesses/{id}/reviews")
    suspend fun getRestaurantReviews(
        @Header("Authorization") token: String,
        @Path("id") id:String
    ):Reviews
    @GET("businesses/search")
    suspend fun getNearbyRestaurants(
        @Header("Authorization") token: String,
        @Query("term") searchTerm:String,
        @Query("location") location:String,
        @Query("latitude") latitude:Double,
        @Query("longitude") longitude:Double,
        @Query("radius") radius:Int
    ):Restaurants
}

// TODO: Create an object that provides a lazy-initialized retrofit service
object RestaurantApi {
    val retrofitService : RestaurantApiService by lazy {
        retrofit.create(RestaurantApiService::class.java) }
}