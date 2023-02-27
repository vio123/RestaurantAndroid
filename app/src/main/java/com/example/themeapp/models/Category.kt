package com.example.themeapp.models

import com.squareup.moshi.Json

data class Category(
    @Json(name = "alias")
    val alias:String,
    @Json(name = "title")
    val title:String
)
