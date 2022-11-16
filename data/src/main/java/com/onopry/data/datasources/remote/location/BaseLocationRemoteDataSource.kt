package com.onopry.data.datasources.remote.location

import com.onopry.data.model.CityLocationResponse
import retrofit2.Response

interface BaseLocationRemoteDataSource {
    suspend fun identifyLocality(
        lat: String,
        lon: String
    ): Response<List<CityLocationResponse>>
}

class BaseLocationRemoteDataSourceImpl(
    private val api: LocationApi
) : BaseLocationRemoteDataSource {
    override suspend fun identifyLocality(
        lat: String,
        lon: String
    ) = api.getLocation(lat, lon)
}