package com.onopry.data.repository

import com.onopry.data.datasources.remote.RemoteDataSource
import com.onopry.data.model.toDomainModel
import com.onopry.domain.model.forecast.Forecast
import com.onopry.domain.repository.Repository
import com.onopry.domain.utils.ApiError
import com.onopry.domain.utils.ApiException
import com.onopry.domain.utils.ApiResult
import com.onopry.domain.utils.ApiSuccess
import kotlinx.coroutines.flow.flow

class ForecastRepository(private val forecastDataSource: RemoteDataSource) : Repository {
    override fun getForecast(
        lat: String,
        lon: String,
        startDate: String,
        endDate: String
    ) = flow<ApiResult<Forecast>> {
        val response = forecastDataSource.getForecast(lat, lon, startDate, endDate)
        response.collect { result ->
            when(result) {
                is ApiSuccess -> emit(ApiSuccess(data = result.data.toDomainModel()))
                is ApiError -> emit(ApiError(code = result.code, message = result.message))
                is ApiException -> emit(ApiException(message = result.message))
            }
        }
    }
}