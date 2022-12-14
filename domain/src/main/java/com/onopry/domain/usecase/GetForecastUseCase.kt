package com.onopry.domain.usecase

import com.onopry.domain.repository.Repository
import kotlinx.coroutines.flow.map

class GetForecastUseCase(private val repository: Repository) {
    operator fun invoke(
        lat: Float,
        lon: Float,
        startDate: String,
        endDate: String
    ) = repository.getForecast(lat, lon, startDate, endDate)
}