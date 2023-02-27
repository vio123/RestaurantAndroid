package com.example.themeapp.models

import com.squareup.moshi.Json

data class Location(
    @Json(name = "display_address")
    val display_address:List<String>
)
