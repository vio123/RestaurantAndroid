package com.example.themeapp.network

import com.example.themeapp.models.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL="https://us-central1-nodejs-a0f94.cloudfunctions.net/app/"

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
    @POST("api/addUser")
    suspend fun addUser(
        @Body requestBody: Utilizator
    )

    @POST("api/addReview")
    suspend fun addReview(
        @Body requestBody:Review,
        @Header("authorization") token: String
    )
    // TODO: Declare a suspended function to get the list of restaurants
    @GET("api/getRestaurants")
    suspend fun getRestaurants(
        @Header("authorization") token:String,
        @Query("location") location:String,
    ): Restaurants

    @GET("api/{idUser}")
    suspend fun getUser(
        @Header("authorization") token: String,
        @Path("idUser") idUser:String
    ):User

    @GET("businesses/search")
    suspend fun getRestaurantsByFilter(
        @Header("Authorization") token:String,
        @Query("term") searchTerm:String,
        @Query("location") location: String,
        @Query("limit") limit:Int,
        @Query("sort_by") sortBy:String
    ):Restaurants
    @GET("api/getDetailsRestaurant/{id}")
    suspend fun getRestaurantDetail(
        @Header("authorization") token:String,
        @Path("id") searchById:String
    ):RestaurantDetails
    @GET("api/{id}/ratings")
    suspend fun getRestaurantReviews(
        @Header("authorization") token: String,
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