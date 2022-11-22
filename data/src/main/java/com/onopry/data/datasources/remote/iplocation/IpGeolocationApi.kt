package com.onopry.data.datasources.remote.iplocation

import com.onopry.data.model.CityByIpLocalityResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Need for getting user location if permission for identify geolocation is denied
 * */

interface IpGeolocationApi {
    @GET("./ipgeo")
    suspend fun getLocationByIp(): Response<CityByIpLocalityResponse>
}