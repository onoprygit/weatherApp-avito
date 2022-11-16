package com.onopry.weatherapp_avito.presentation.model

import java.time.LocalDate

class ForecastPeriod() {
    val startDate: LocalDate = LocalDate.now()
    val endDate: LocalDate = startDate.plusWeeks(1)
}
