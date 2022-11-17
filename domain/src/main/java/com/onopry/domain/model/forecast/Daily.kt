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
    val apparentTemperatureMax: String,
    val apparentTemperatureMin: String,
    val precipitationHours: String,
    val precipitationSum: String,
    val sunrise: String,
    val sunset: String,
    val temperatureMax: String,
    val temperatureMin: String,
    val time: String,
    val weatherCode: Int,
    val windSpeedMax: String
)

