package com.onopry.domain.model.forecast

data class HourlyUnits(
    val apparentTemperature: String,
    val freezingLevelHeight: String,
    val precipitation: String,
    val rain: String,
    val relativeHumidity: String,
    val showers: String,
    val snowfall: String,
    val temperature: String,
    val time: String,
    val visibility: String,
    val weatherCode: String,
    val windDirection: String,
    val windSpeed: String
    /*    val snow_depth: String,*/
)