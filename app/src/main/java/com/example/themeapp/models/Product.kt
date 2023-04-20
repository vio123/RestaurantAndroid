package com.example.themeapp.models

import com.squareup.moshi.Json

data class Product(
    @Json(name = "name")
    val name:String,
    @Json(name = "price")
    val price:Double,
    @Json(name = "description")
    val description:String,
    @Json(name = "category")
    val category:String,
    @Json(name = "image_url")
    val image_url:String,
    @Json(name = "id")
    val id:String
)
