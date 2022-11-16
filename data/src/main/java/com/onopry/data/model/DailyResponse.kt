package com.onopry.data.model

import com.onopry.domain.model.forecast.Daily
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.math.roundToInt

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

fun DailyResponse.toDomainModel(hourly: HourlyResponse): List<Daily> {
    return time.mapIndexed { index, dailyTime ->
        Daily(
            apparentTemperatureMax = apparentTemperatureMax[index].roundToInt(),
            apparentTemperatureMin = apparentTemperatureMin[index].roundToInt(),
            precipitationHours = precipitationHours[index].roundToInt(),
            precipitationSum = precipitationSum[index].roundToInt(),
            sunrise = sunrise[index],
            sunset = sunset[index],
            temperatureMax = temperatureMax[index].roundToInt(),
            temperatureMin = temperatureMin[index].roundToInt(),
            time = dailyTime,
            weatherCode = weatherCode[index],
            windSpeedMax = windSpeedMax[index].roundToInt(),
            hourlyWeather = hourly.toMy(dailyTime)
        )
    }
}



/*() = Daily(
    apparentTemperatureMax = apparentTemperatureMax.map { it.roundToInt() },
    apparentTemperatureMin = apparentTemperatureMin.map { it.roundToInt() },
    precipitationHours = precipitationHours.map { it.roundToInt() },
    precipitationSum = precipitationSum,
    sunrise = sunrise,
    sunset = sunset,
    temperatureMax = temperatureMax.map { it.roundToInt() },
    temperatureMin = temperatureMin.map { it.roundToInt() },
    time = time,
    weatherCode = weatherCode,
    windSpeedMax = windSpeedMax.map { it.roundToInt() }
)*/

/*

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


*/