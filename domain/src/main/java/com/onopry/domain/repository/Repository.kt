package com.onopry.domain.repository

import com.onopry.domain.model.forecast.Forecast
import com.onopry.domain.utils.ApiResult
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getForecast(
        lat: String,
        lon: String,
        startDate: String,
        endDate: String
    ): Flow<ApiResult<Forecast>>
}