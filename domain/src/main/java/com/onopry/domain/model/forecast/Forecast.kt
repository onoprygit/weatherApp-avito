package com.onopry.domain.model.forecast

data class Forecast(
    val currentWeather: CurrentWeather,
    val daily: Daily,
    val dailyUnits: DailyUnits,
    val elevation: Double,
    val generationTimeMs: Double,
    val hourly: Hourly,
    val hourlyUnits: HourlyUnits,
//    val city: City,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezoneAbbreviation: String,
    val utcOffsetSeconds: Int
)