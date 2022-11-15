package com.onopry.data.model

import com.onopry.domain.model.forecast.Daily
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyResponse(
    @Json(name = "apparent_temperature_max")
    val apparentTemperatureMax: List<Double>,
    @Json(name = "apparent_temperature_min")
    val apparentTemperatureMin: List<Double>,
    @Json(name = "precipitation_hours")
    val precipitationHours: List<Double>,
    @Json(name = "precipitation_sum")
    val precipitationSum: List<Double>,
    val sunrise: List<String>,
    val sunset: List<String>,
    @Json(name = "temperature_2m_max")
    val temperatureMax: List<Double>,
    @Json(name = "temperature_2m_min")
    val temperatureMin: List<Double>,
    val time: List<String>,
    @Json(name = "weathercode")
    val weatherCode: List<Int>,
    @Json(name = "windspeed_10m_max")
    val windSpeedMax: List<Double>
)

fun DailyResponse.toDomainModel() = Daily(
    apparentTemperatureMax,
    apparentTemperatureMin,
    precipitationHours,
    precipitationSum,
    sunrise,
    sunset,
    temperatureMax,
    temperatureMin,
    time,
    weatherCode,
    windSpeedMax
)