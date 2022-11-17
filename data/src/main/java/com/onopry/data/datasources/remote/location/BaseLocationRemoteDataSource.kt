package com.onopry.data.datasources.remote.location

import com.onopry.data.model.CityLocationResponse
import com.onopry.data.model.CityResponse
import retrofit2.Response

interface BaseLocationRemoteDataSource {
    suspend fun identifyLocality(
        lat: String,
        lon: String
    ): Response<List<CityLocationResponse>>

    suspend fun searchCities(query: String): Response<CityResponse>
}

class BaseLocationRemoteDataSourceImpl(
    private val api: LocationApi
) : BaseLocationRemoteDataSource {
    override suspend fun identifyLocality(
        lat: String,
        lon: String
    ) = api.getLocation(lat, lon)

    override suspend fun searchCities(
        query: String
    ) = api.getCities(query)
}