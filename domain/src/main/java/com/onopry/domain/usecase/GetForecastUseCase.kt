package com.onopry.domain.usecase

import com.onopry.domain.repository.Repository

class GetForecastUseCase(private val repository: Repository) {
    operator fun invoke(
        lat: Float,
        lon: Float,
        startDate: String,
        endDate: String
    ) = repository.getForecast(lat, lon, startDate, endDate)
}