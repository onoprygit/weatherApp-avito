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
    val temperature: Int,
    val time: String,
    val weatherCode: Int,

    val apparentTemperature: Double,
    val freezingLevelHeight: Double,
    val precipitation: Double,
    val rain: Double,
    val relativeHumidity: Int,
    val showers: Double,
    val snowfall: Double,
    val visibility: Double,
    val windDirection: Int,
    val windSpeed: Double
)



