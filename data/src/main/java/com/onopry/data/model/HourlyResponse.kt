package com.onopry.data.model

import com.onopry.domain.model.forecast.Hourly
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourlyResponse(
    @Json(name = "apparent_temperature")
    val apparentTemperature: List<Double>,
    @Json(name = "freezinglevel_height")
    val freezingLevelHeight: List<Double>,
    val precipitation: List<Double>,
    val rain: List<Double>,
    @Json(name = "relativehumidity_2m")
    val relativeHumidity: List<Int>,
    val showers: List<Double>,
    /*    @Json(name = "snow_depth")
        val snowDepth: List<Double>,*/
    val snowfall: List<Double>,
    @Json(name = "temperature_2m")
    val temperature_2m: List<Double>,
    val time: List<String>,
    val visibility: List<Double>,
    @Json(name = "weathercode")
    val weatherCode: List<Int>,
    @Json(name = "winddirection_10m")
    val windDirection: List<Int>,
    @Json(name = "windspeed_10m")
    val windSpeed: List<Double>
)


fun HourlyResponse.toDomainModel() = Hourly(
    apparentTemperature,
    freezingLevelHeight,
    precipitation,
    rain,
    relativeHumidity,
    showers,
    snowfall,
    temperature_2m,
    time,
    visibility,
    weatherCode,
    windDirection,
    windSpeed
)