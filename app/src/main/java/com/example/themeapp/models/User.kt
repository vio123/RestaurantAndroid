package com.example.themeapp.models

import com.squareup.moshi.Json

data class User(
    @Json(name = "idUser")
    val idUser:String="",
    @Json(name = "name")
    val name:String="",
    @Json(name = "email")
    val email:String=""
)
