package com.onopry.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentWeatherResponse(
    val temperature: Double,
    val time: String,
    @Json(name = "weathercode")
    val weatherCode: Int,
    @Json(name = "winddirection")
    val windDirection: Double,
    @Json(name = "windspeed")
    val windSpeed: Double
)