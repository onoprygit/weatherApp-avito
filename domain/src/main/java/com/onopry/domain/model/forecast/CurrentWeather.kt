package com.onopry.domain.model.forecast

data class CurrentWeather(
    val temperature: Double,
    val time: String,
    val weatherCode: Int,
    val windDirection: Double,
    val windSpeed: Double
)
