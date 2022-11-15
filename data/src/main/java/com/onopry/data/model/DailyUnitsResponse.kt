package com.onopry.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyUnitsResponse(
    @Json(name = "apparent_temperature_max")
    val apparentTemperatureMax: String,
    @Json(name = "apparent_temperature_min")
    val apparentTemperatureMin: String,
    @Json(name = "precipitation_hours")
    val precipitationHours: String,
    @Json(name = "precipitation_sum")
    val precipitationSum: String,
    val sunrise: String,
    val sunset: String,
    @Json(name = "temperature_2m_max")
    val temperatureMax: String,
    @Json(name = "temperature_2m_min")
    val temperatureMin: String,
    val time: String,
    @Json(name = "weathercode")
    val weatherCode: String,
    @Json(name = "windspeed_10m_max")
    val windSpeedMax: String
)