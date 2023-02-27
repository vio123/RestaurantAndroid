package com.example.themeapp.models

import com.squareup.moshi.Json

data class Hours(
    @Json(name = "open")
    val open:List<Hour>,
    @Json(name = "hours_type")
    val hoursType:String,
    @Json(name = "is_open_now")
    val isOpenNow:Boolean,
)
