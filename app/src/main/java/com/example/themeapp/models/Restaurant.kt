package com.example.themeapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
data class Restaurant(
   @Json(name = "id")
   val id:String="",
   @Json(name = "name")
   val name:String="",
   @Json(name = "image_url")
   val img:String="",
   @Json(name = "rating")
   val rating:Double=0.0,
   @Json(name = "location")
   val location: Location= Location(emptyList()),
   @Json(name = "review_count")
   val reviewCount: Int=0,
   @Json(name = "categories")
   val categories:List<Category> = emptyList(),
   @Json(name = "coordinates")
   val coordinates:Coordinates=Coordinates(0.0,0.0)
)
