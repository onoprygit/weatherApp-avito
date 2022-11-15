package com.onopry.data.datasources.remote.api

import com.onopry.data.model.ipLocationModel.IpGeolocationResponse
import retrofit2.Response
import retrofit2.http.GET
//https://api.ipgeolocation.io/ipgeo?apiKey=07e338975c4945aea23b82ca3dfeb7e7
interface IpGeolocationApi {
    @GET("./ipgeo")
    suspend fun getLocationByIp(): Response<IpGeolocationResponse>
}