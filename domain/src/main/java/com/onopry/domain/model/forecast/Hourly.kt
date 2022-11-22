package com.onopry.domain.model.forecast

data class Hourly(
    val temperature: String,
    val time: String,
    val weatherCode: Int,
    val apparentTemperature: String,
    val freezingLevelHeight: String,
    val precipitation: String,
    val rain: String,
    val relativeHumidity: String,
    val showers: String,
    val snowfall: String,
    val visibility: String,
    val windDirection: String,
    val windSpeed: String
)



