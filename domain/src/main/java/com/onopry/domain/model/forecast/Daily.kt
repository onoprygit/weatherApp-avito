package com.onopry.domain.model.forecast

/*data class Daily(
    val apparentTemperatureMax: List<Int>,
    val apparentTemperatureMin: List<Int>,
    val precipitationHours: List<Int>,
    val precipitationSum: List<Double>,
    val sunrise: List<String>,
    val sunset: List<String>,
    val temperatureMax: List<Int>,
    val temperatureMin: List<Int>,
    val time: List<String>,
    val weatherCode: List<Int>,
    val windSpeedMax: List<Int>
)

// должен быть одним днем в списке*/
data class Daily(
    val hourlyWeather: List<Hourly>,
    val apparentTemperatureMax: Int,
    val apparentTemperatureMin: Int,
    val precipitationHours: Int,
    val precipitationSum: Int,
    val sunrise: String,
    val sunset: String,
    val temperatureMax: Int,
    val temperatureMin: Int,
    val time: String,
    val weatherCode: Int,
    val windSpeedMax: Int
)

