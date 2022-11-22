package com.onopry.domain.repository

import com.onopry.domain.model.forecast.Locality
import com.onopry.domain.model.forecast.Forecast
import com.onopry.domain.model.forecast.LocalitySearch
import com.onopry.domain.utils.ApiResult
import kotlinx.coroutines.flow.Flow

interface Repository {
/*    fun getForecast(
        lat: String,
        lon: String,
        startDate: String,
        endDate: String
    ): Flow<ApiResult<Forecast>>*/

    fun getLocationName(lat: String, lon: String): Flow<ApiResult<Locality>>
    fun getLocationByIp(): Flow<ApiResult<Locality>>
    fun searchCities(query: String): Flow<ApiResult<List<LocalitySearch>>>
    fun getForecast(
        lat: Float,
        lon: Float,
        startDate: String,
        endDate: String
    ): Flow<ApiResult<Forecast>>
}