package com.onopry.domain.model.forecast

data class Forecast(
    val currentWeather: CurrentWeather,
    val dailyWeather: List<Daily>,
    val dailyUnits: DailyUnits,
    val hourlyUnits: HourlyUnits,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
)