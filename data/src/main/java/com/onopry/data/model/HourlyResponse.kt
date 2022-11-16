package com.onopry.data.model

import com.onopry.domain.model.forecast.Hourly
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.time.LocalDateTime
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



fun HourlyResponse.toDomainModel(dayDate: String): List<Hourly> {
    val rootDate = LocalDate.parse(dayDate)
    return time.mapIndexed { index, time ->
        Hourly(
            temperature = temperature_2m[index].roundToInt(),
            time = time,
            weatherCode = weatherCode[index],
            apparentTemperature = apparentTemperature[index],
            freezingLevelHeight = freezingLevelHeight[index],
            precipitation = precipitation[index],
            rain = rain[index],
            relativeHumidity = relativeHumidity[index],
            showers = showers[index],
            snowfall = snowfall[index],
            visibility = visibility[index],
            windDirection = windDirection[index],
            windSpeed = windSpeed[index]
        )
    }.filter {
        val hourlyDate = LocalDateTime.parse(it.time)
        rootDate.year == hourlyDate.year && rootDate.dayOfYear == hourlyDate.dayOfYear
    }
}


/*
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
)*/
