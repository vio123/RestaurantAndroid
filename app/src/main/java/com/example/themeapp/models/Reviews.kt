package com.example.themeapp.models

import com.squareup.moshi.Json

data class Reviews(
    @Json(name = "reviews")
    val reviews:List<Review>
)
