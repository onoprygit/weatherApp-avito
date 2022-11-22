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
    val rain: List<Double?>,
    @Json(name = "relativehumidity_2m")
    val relativeHumidity: List<Int>,
    val showers: List<Double>,
    val snowfall: List<Double>,
    @Json(name = "temperature_2m")
    val temperature_2m: List<Double>,
    val time: List<String>,
    val visibility: List<Double>,
    @Json(name = "weathercode")
    val weatherCode: List<Int?>,
    @Json(name = "winddirection_10m")
    val windDirection: List<Int>,
    @Json(name = "windspeed_10m")
    val windSpeed: List<Double>
)


fun HourlyResponse.toDomainModel(dayDate: String, units: HourlyUnitsResponse): List<Hourly> {
    val rootDate = LocalDate.parse(dayDate)
    return time.mapIndexed { index, time ->
        Hourly(
            temperature = temperature_2m[index].roundToInt().toString() + units.temperature,
            time = time,
            weatherCode = weatherCode[index] ?: 0,
            apparentTemperature = apparentTemperature[index].toString() + units.apparentTemperature,
            freezingLevelHeight = freezingLevelHeight[index].toString() + units.freezingLevelHeight,
            precipitation = precipitation[index].toString() + " " + units.precipitation,
            rain = rain[index].toString() + " " + units.rain,
            relativeHumidity = relativeHumidity[index].toString() + units.relativeHumidity,
            showers = showers[index].toString() + " " + units.showers,
            snowfall = snowfall[index].toString() + " " + units.snowfall,
            visibility = visibility[index].toString() + " " + units.visibility,
            windDirection = windDirection[index].toString() + units.windDirection,
            windSpeed = windSpeed[index].toString() + " " + units.windSpeed
        )
    }.filter {
        val hourlyDate = LocalDateTime.parse(it.time)
        rootDate.year == hourlyDate.year && rootDate.dayOfYear == hourlyDate.dayOfYear
    }
}
