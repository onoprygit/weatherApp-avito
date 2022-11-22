package com.onopry.data.datasources.remote.location

import com.onopry.data.model.CityLocalityResponse
import retrofit2.Response

interface BaseLocationRemoteDataSource {
    suspend fun identifyLocality(
        lat: String,
        lon: String
    ): Response<List<CityLocalityResponse>>

    suspend fun searchCities(query: String): Response<List<CityLocalityResponse>>
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
    ) = api.searchLocation(query)
}