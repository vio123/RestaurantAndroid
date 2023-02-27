package com.example.themeapp.models

import com.squareup.moshi.Json

data class RestaurantDetails(
    @Json(name = "id")
    val id:String = "",
    @Json(name = "name")
    val name:String = "",
    @Json(name = "image_url")
    val img:String = "",
    @Json(name = "is_claimed")
    val isClaimed:Boolean = false,
    @Json(name = "is_closed")
    val isClosed:Boolean = false,
    @Json(name = "phone")
    val phone:String = "",
    @Json(name = "review_count")
    val reviewCount:Int = 0,
    @Json(name = "categories")
    val categories:List<Category> = emptyList(),
    @Json(name = "rating")
    val rating:Double = 0.0,
    @Json(name = "location")
    val location: Location? = null,
    @Json(name = "coordinates")
    val coordinates:Coordinates,
    @Json(name = "photos")
    val photos:List<String> = emptyList(),
    @Json(name = "hours")
    val hours:List<Hours> = emptyList(),
    @Json(name = "price")
    val price:String = ""
)
