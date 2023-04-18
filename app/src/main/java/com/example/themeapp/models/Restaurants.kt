package com.example.themeapp.models

import com.squareup.moshi.Json

data class Restaurants(
    @Json(name = "restaurants")
    val restaurant:List<Restaurant>
)
