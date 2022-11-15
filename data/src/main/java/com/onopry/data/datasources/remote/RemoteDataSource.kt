package com.onopry.data.datasources.remote

import com.onopry.data.model.ForecastResponse
import com.onopry.domain.utils.ApiResult
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun getForecast(
        lat: String,
        lon: String,
        startDate: String,
        endDate: String
    ): Flow<ApiResult<ForecastResponse>>
}