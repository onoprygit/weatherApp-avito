package com.onopry.domain.model.forecast

data class Hourly(
    val apparentTemperature: List<Double>,
    val freezingLevelHeight: List<Double>,
    val precipitation: List<Double>,
    val rain: List<Double>,
    val relativeHumidity: List<Int>,
    val showers: List<Double>,
    val snowfall: List<Double>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val visibility: List<Double>,
    val weatherCode: List<Int>,
    val windDirection: List<Int>,
    val windSpeed: List<Double>
    /*  val snowDepth: List<Double>,*/
)