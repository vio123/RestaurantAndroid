package com.example.themeapp.models

import com.squareup.moshi.Json

data class Review(
    @Json(name = "id")
    val id:String,
    @Json(name = "rating")
    val rating:Double,
    @Json(name = "user")
    val user:User,
    @Json(name = "text")
    val text:String,
    @Json(name = "time_created")
    val timeCreated:String
)
