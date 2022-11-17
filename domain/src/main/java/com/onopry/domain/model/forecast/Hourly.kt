package com.onopry.domain.model.forecast

/*
data class Hourly(
    val apparentTemperature: List<Int>,
    val freezingLevelHeight: List<Int>,
    val precipitation: List<Double>,
    val rain: List<Double>,
    val relativeHumidity: List<Int>,
    val showers: List<Double>,
    val snowfall: List<Double>,
    val temperature_2m: List<Int>,
    val time: List<String>,
    val visibility: List<Int>,
    val weatherCode: List<Int>,
    val windDirection: List<Int>,
    val windSpeed: List<Int>
)
*/

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



