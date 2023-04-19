package com.example.themeapp.models

import com.squareup.moshi.Json

data class Reviews(
    @Json(name = "reviews")
    var reviews:List<Review>
)
