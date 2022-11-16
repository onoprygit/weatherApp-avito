package com.onopry.data.model

import com.onopry.domain.model.forecast.HourlyUnits
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourlyUnitsResponse(
    @Json(name = "apparent_temperature")
    val apparentTemperature: String,
    @Json(name = "freezinglevel_height")
    val freezingLevelHeight: String,
    val precipitation: String,
    val rain: String,
    @Json(name = "relativehumidity_2m")
    val relativeHumidity: String,
    val showers: String,
    /*    val snow_depth: String,*/
    val snowfall: String,
    @Json(name = "temperature_2m")
    val temperature: String,
    val time: String,
    val visibility: String,
    @Json(name = "weathercode")
    val weatherCode: String,
    @Json(name = "winddirection_10m")
    val windDirection: String,
    @Json(name = "windspeed_10m")
    val windSpeed: String
)

fun HourlyUnitsResponse.toDomainModel() = HourlyUnits(
    apparentTemperature,
    freezingLevelHeight,
    precipitation,
    rain,
    relativeHumidity,
    showers,
    snowfall,
    temperature,
    time,
    visibility,
    weatherCode,
    windDirection,
    windSpeed
)