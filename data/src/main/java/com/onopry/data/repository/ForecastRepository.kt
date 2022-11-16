package com.onopry.data.repository

import android.util.Log
import com.onopry.data.datasources.remote.forecast.ForecastRemoteDataSource
import com.onopry.data.datasources.remote.iplocation.IpLocationRemoteDataSource
import com.onopry.data.datasources.remote.location.BaseLocationRemoteDataSource
import com.onopry.data.model.toDomainModel
import com.onopry.domain.repository.Repository
import com.onopry.domain.utils.ApiError
import com.onopry.domain.utils.ApiException
import com.onopry.domain.utils.ApiSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class ForecastRepository(
    private val forecastSource: ForecastRemoteDataSource,
    private val baseLocationSource: BaseLocationRemoteDataSource,
    private val ipLocationSource: IpLocationRemoteDataSource
) : Repository {

    override fun getForecast(
        lat: String,
        lon: String,
        startDate: String,
        endDate: String
    ) = flow {
        try {
            val forecastResponse = forecastSource.getForecast(lat, lon, startDate, endDate)

            val body = forecastResponse.body()

            if (forecastResponse.isSuccessful && body != null) {
                emit(ApiSuccess(data = body.toDomainModel()))
            } else {
                emit(ApiError(code = forecastResponse.code(), message = forecastResponse.message()))
            }
        } catch (e: HttpException) {
            emit(ApiError(code = e.code(), e.message()))
        } catch (e: Exception) {
            emit(ApiException(message = e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override fun getLocationName(lat: String, lon: String) = flow {
        try {
            val locationResponce = baseLocationSource.identifyLocality(lat, lon)

            val body = locationResponce.body()?.get(0)

            if (locationResponce.isSuccessful && body != null) {
                emit(ApiSuccess(data = body.toDomainModel()))
            } else {
                emit(ApiError(code = locationResponce.code(), message = locationResponce.message()))
            }
        } catch (e: HttpException) {
            emit(ApiError(code = e.code(), e.message()))
        } catch (e: Exception) {
            Log.e("DEV_", "getLocationName: ${e.message}", )
            emit(ApiException(message = e.stackTrace.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override fun getLocationByIp() = flow {
        try {
            val ipLocationResponse = ipLocationSource.identifyLocationByIp()

            val body = ipLocationResponse.body()

            if (ipLocationResponse.isSuccessful && body != null) {
                emit(ApiSuccess(data = body.toDomainModel()))
            } else {
                emit(
                    ApiError(
                        code = ipLocationResponse.code(),
                        message = ipLocationResponse.message()
                    )
                )
            }
        } catch (e: HttpException) {
            emit(ApiError(code = e.code(), e.message()))
        } catch (e: Exception) {
            emit(ApiException(message = e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}


