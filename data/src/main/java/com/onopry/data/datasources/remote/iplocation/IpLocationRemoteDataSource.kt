package com.onopry.data.datasources.remote.iplocation

import com.onopry.data.model.CityByIpLocalityResponse
import retrofit2.Response

interface IpLocationRemoteDataSource {
    suspend fun identifyLocationByIp(): Response<CityByIpLocalityResponse>
}

class IpLocationRemoteDataSourceImpl(
    private val api: IpGeolocationApi
) : IpLocationRemoteDataSource {
    override suspend fun identifyLocationByIp() = api.getLocationByIp()
}