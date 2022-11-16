package com.onopry.data.datasources.remote.forecast

import com.onopry.data.model.ForecastResponse
import retrofit2.Response


/*
* Now this class just passes data between layers
*/

interface ForecastRemoteDataSource {

    suspend fun getForecast(
        lat: String,
        lon: String,
        startDate: String,
        endDate: String
    ): Response<ForecastResponse>
}

class ForecastApiDataSource(
    private val forecastApi: ForecastApi
) : ForecastRemoteDataSource {

    override suspend fun getForecast(lat: String, lon: String, startDate: String, endDate: String):
            Response<ForecastResponse> =
        forecastApi.getComplexForecast(lat, lon, startDate, endDate)
}