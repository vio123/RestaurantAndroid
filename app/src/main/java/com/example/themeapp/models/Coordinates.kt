package com.example.themeapp.models

import com.squareup.moshi.Json

data class Coordinates(
    @Json(name = "latitude")
    val latitude:Double,
    @Json(name = "longitude")
    val longitude:Double
)
