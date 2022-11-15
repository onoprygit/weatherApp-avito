package com.onopry.data.datasources.remote

import com.onopry.data.datasources.remote.api.ForecastApi
import com.onopry.data.datasources.remote.api.LocationApi
import com.onopry.domain.utils.ApiError
import com.onopry.domain.utils.ApiException
import com.onopry.domain.utils.ApiSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class ForecastApiDataSource(
    private val forecastApi: ForecastApi,
    private val locationApi: LocationApi
) : RemoteDataSource {

    override suspend fun getForecast(
        lat: String,
        lon: String,
        startDate: String,
        endDate: String
    ) = flow {
        try {
            val response = forecastApi.getComplexForecast(lat, lon, startDate, endDate)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                emit(ApiSuccess(data = body))
            } else {
                emit(ApiError(code = response.code(), message = response.message()))
            }
        } catch (e: HttpException) {
            emit(ApiError(code = e.code(), e.message()))
        } catch (e: Exception) {
            emit(ApiException(message = e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}