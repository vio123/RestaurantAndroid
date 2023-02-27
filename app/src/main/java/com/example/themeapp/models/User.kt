package com.example.themeapp.models

import com.squareup.moshi.Json

data class User(
    @Json(name = "image_url")
    val imageUrl:String,
    @Json(name = "name")
    val name:String
)
