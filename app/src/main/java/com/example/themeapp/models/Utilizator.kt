package com.example.themeapp.models

import com.squareup.moshi.Json

data class Utilizator(
    @Json(name="name")
    val name:String,
    @Json(name = "email")
    val email:String,
    @Json(name = "password")
    val password:String
)
