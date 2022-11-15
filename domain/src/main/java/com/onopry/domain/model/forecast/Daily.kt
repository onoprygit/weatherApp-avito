package com.onopry.domain.model.forecast

data class Daily(
    val apparentTemperatureMax: List<Double>,
    val apparentTemperatureMin: List<Double>,
    val precipitationHours: List<Double>,
    val precipitationSum: List<Double>,
    val sunrise: List<String>,
    val sunset: List<String>,
    val temperatureMax: List<Double>,
    val temperatureMin: List<Double>,
    val time: List<String>,
    val weatherCode: List<Int>,
    val windSpeedMax: List<Double>
)