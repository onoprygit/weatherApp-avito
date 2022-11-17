package com.onopry.weatherapp_avito.presentation.model

import com.onopry.domain.model.forecast.Daily
import com.onopry.domain.model.forecast.Hourly
import java.util.*

data class DailyUi(
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
    val windSpeedMax: String,
    var isExpanded: Boolean,
    val id: UUID = UUID.randomUUID()
)

