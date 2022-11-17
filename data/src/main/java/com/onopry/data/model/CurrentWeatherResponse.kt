package com.onopry.data.model

import com.onopry.domain.model.forecast.CurrentWeather
import com.onopry.domain.model.forecast.HourlyUnits
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.math.roundToInt

@JsonClass(generateAdapter = true)
data class CurrentWeatherResponse(
    val temperature: Double,
    val time: String,
    @Json(name = "weathercode")
    val weatherCode: Int?,
    @Json(name = "winddirection")
    val windDirection: Double,
    @Json(name = "windspeed")
    val windSpeed: Double
)

// Probably better if numeric types convert to String for recycler performance
fun CurrentWeatherResponse.toDomainModel(units: HourlyUnitsResponse) = CurrentWeather(
    temperature = temperature.toString() + units.temperature,
    time = time,
    weatherCode = weatherCode ?: 0,
    windDirection = "$windDirection${units.windDirection}",
    windSpeed = "${windSpeed.roundToInt()} ${units.windSpeed}"
)