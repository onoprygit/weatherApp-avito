package com.onopry.domain.model.forecast

data class Daily(
    val apparentTemperatureMax: List<Int>,
    val apparentTemperatureMin: List<Int>,
    val precipitationHours: List<Int>,
    val precipitationSum: List<Double>,
    val sunrise: List<String>,
    val sunset: List<String>,
    val temperatureMax: List<Int>,
    val temperatureMin: List<Int>,
    val time: List<String>,
    val weatherCode: List<Int>,
    val windSpeedMax: List<Int>
)