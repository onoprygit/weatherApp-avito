package com.onopry.data.model

import com.onopry.domain.model.forecast.Daily
import com.onopry.domain.model.forecast.DailyUnits
import com.onopry.domain.model.forecast.HourlyUnits
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
    val weatherCode: List<Int?>,
    @Json(name = "windspeed_10m_max")
    val windSpeedMax: List<Double>
)

fun DailyResponse.toDomainModel(hourly: HourlyResponse, units: DailyUnitsResponse, hourlyUnits: HourlyUnitsResponse): List<Daily> {
    return time.mapIndexed { index, dailyTime ->
        Daily(
            apparentTemperatureMax = apparentTemperatureMax[index].roundToInt().toString() + units.apparentTemperatureMax,
            apparentTemperatureMin = apparentTemperatureMin[index].roundToInt().toString() + units.apparentTemperatureMax,
            precipitationHours = precipitationHours[index].roundToInt().toString() + " " + units.precipitationHours,
            precipitationSum = precipitationSum[index].roundToInt().toString() + " " + units.precipitationSum,
            sunrise = sunrise[index],
            sunset = sunset[index],
            temperatureMax = temperatureMax[index].roundToInt().toString() + units.apparentTemperatureMax,
            temperatureMin = temperatureMin[index].roundToInt().toString() + units.apparentTemperatureMax,
            time = dailyTime,
            weatherCode = weatherCode[index] ?: 0,
            windSpeedMax = windSpeedMax[index].roundToInt().toString() + " " + units.windSpeedMax,
            hourlyWeather = hourly.toDomainModel(dailyTime, hourlyUnits)
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