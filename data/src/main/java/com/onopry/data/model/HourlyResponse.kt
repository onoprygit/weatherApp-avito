package com.onopry.data.model

import com.onopry.domain.model.forecast.Hourly
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.math.roundToInt

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
    apparentTemperature = apparentTemperature.map { it.roundToInt() },
    freezingLevelHeight = freezingLevelHeight.map { it.roundToInt() },
    precipitation = precipitation,
    rain = rain,
    relativeHumidity = relativeHumidity,
    showers = showers,
    snowfall = snowfall,
    temperature_2m = temperature_2m.map { it.roundToInt() },
    time = time,
    visibility = visibility.map { it.roundToInt() },
    weatherCode = weatherCode,
    windDirection = windDirection,
    windSpeed = windSpeed.map { it.roundToInt() }
)