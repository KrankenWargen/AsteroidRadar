package com.udacity.asteroidradar

import com.squareup.moshi.Json

data class PictureOfDay(
    @Json(name = "title") val title: String,
    @Json(name = "url") val url: String,
    @Json(name = "date") val date: String
)

