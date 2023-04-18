package com.example.themeapp.models

import com.squareup.moshi.Json

data class Location(
    @Json(name = "address1")
    val address1:String="",
    @Json(name = "city")
    val city:String="",
    @Json(name = "zip_code")
    val zip_code:String="",
    val display_address:List<String> = listOf(address1,city,zip_code)
)
