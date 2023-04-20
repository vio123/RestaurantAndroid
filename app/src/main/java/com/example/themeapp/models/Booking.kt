package com.example.themeapp.models

import com.squareup.moshi.Json

data class Booking(
    @Json(name = "id")
    val id:String,
    @Json(name = "idRestaurant")
    val idRestaurant:String,
    @Json(name = "idClient")
    val idClient:String,
    @Json(name = "nrPers")
    val nrPers:Int,
    @Json(name = "products")
    val products:List<Product>,
    @Json(name = "status")
    val status:String,
    @Json(name = "datetime")
    val datetime:String
)
