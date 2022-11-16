package com.onopry.data.model

import com.onopry.domain.model.forecast.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.roundToInt

@JsonClass(generateAdapter = true)
data class ForecastResponse(
    @Json(name = "current_weather")
    val currentWeather: CurrentWeatherResponse,
    val daily: DailyResponse,
    @Json(name = "daily_units")
    val dailyUnits: DailyUnitsResponse,
    val elevation: Double,
    @Json(name = "generationtime_ms")
    val generationTimeMs: Double,
    val hourly: HourlyResponse,
    @Json(name = "hourly_units")
    val hourlyUnits: HourlyUnitsResponse,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    @Json(name = "timezone_abbreviation")
    val timezoneAbbreviation: String,
    @Json(name = "utc_offset_seconds")
    val utcOffsetSeconds: Int
)
/*
fun ForecastResponse.toDomainModel() = Forecast(
    currentWeather = currentWeather.toDomainModel(),
    daily = daily.toDomainModel(),
    dailyUnits = dailyUnits.toDomainModel(),
    elevation = elevation.roundToInt(),
    generationTimeMs = generationTimeMs,
    hourly = hourly.toDomainModel(),
    hourlyUnits = hourlyUnits.toDomainModel(),
    latitude, longitude, timezone, timezoneAbbreviation, utcOffsetSeconds,
)*/


/*
fun HourlyUnitsResponse.toMy() = HourlyUnits(
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
*/

fun CurrentWeatherResponse.toMy() = CurrentWeather(
    temperature, time, weatherCode, windDirection, windSpeed
)

fun ForecastResponse.toDomainModel() = Forecast(
    currentWeather = currentWeather.toDomainModel(),
    dailyUnits = dailyUnits.toDomainModel(),
    hourlyUnits = hourlyUnits.toDomainModel(),
    latitude = latitude,
    longitude = longitude,
    timezone = timezone,
    dailyWeather = daily.toDomainModel(hourly)
)

fun DailyResponse.toMy(hourly: HourlyResponse): List<Daily> {
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

fun HourlyResponse.toMy(dayDate: String): List<Hourly> {
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


