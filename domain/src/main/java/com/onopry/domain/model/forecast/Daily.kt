package com.onopry.domain.model.forecast

data class Daily(
    val hourlyWeather: List<Hourly>,
    val apparentTemperatureMax: String,
    val apparentTemperatureMin: String,
    val precipitationHours: String,
    val precipitationSum: String,
    val sunrise: String,
    val sunset: String,
    val temperatureMax: String,
    val temperatureMin: String,
    val time: String,
    val weatherCode: Int,
    val windSpeedMax: String
)

