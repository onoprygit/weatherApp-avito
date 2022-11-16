package com.onopry.data.datasources.remote.iplocation

import com.onopry.data.model.CityByIpLocationResponse
import retrofit2.Response

interface IpLocationRemoteDataSource {
    suspend fun identifyLocationByIp(): Response<CityByIpLocationResponse>
}

class IpLocationRemoteDataSourceImpl(
    private val api: IpGeolocationApi
) : IpLocationRemoteDataSource {
    override suspend fun identifyLocationByIp() = api.getLocationByIp()
}