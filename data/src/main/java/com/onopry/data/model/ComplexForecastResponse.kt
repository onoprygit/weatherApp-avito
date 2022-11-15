package com.onopry.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ComplexForecastResponse(
    @Json(name = "current_weather")
    val currentWeather: CurrentWeatherResponse,
    val daily: DailyResponse,
    @Json(name = "daily_units")
    val daily_units: DailyUnitsResponse,
    val elevation: Double,
    @Json(name = "generationtime_ms")
    val generationTimeMs: Double,
    val hourly: HourlyResponse,
    @Json(name = "hourly_units")
    val hourlyUnits: HourlyUnitsResponse,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    @Json(name = "timezone_abbreviation")
    val timezoneAbbreviation: String,
    @Json(name = "utc_offset_seconds")
    val utcOffsetSeconds: Int
)