package com.example.themeapp.models

import com.squareup.moshi.Json

data class Hour(
    @Json(name = "is_overnight")
    val isOvernight:Boolean,
    @Json(name = "start")
    val start:String,
    @Json(name = "end")
    val end:String,
    @Json(name = "day")
    val day:Int
)
