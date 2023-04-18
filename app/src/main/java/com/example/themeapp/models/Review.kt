package com.example.themeapp.models

import com.squareup.moshi.Json

data class Review(
    @Json(name = "idUser")
    val id:String,
    @Json(name = "rating")
    val rating:Double,
    @Json(name = "message")
    val message:String,
)
