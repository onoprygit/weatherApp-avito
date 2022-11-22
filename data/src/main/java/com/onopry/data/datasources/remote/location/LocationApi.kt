package com.onopry.data.datasources.remote.location

import com.onopry.data.model.CityLocalityResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * It is necessary to understand in which locality the user is located
 * */

interface LocationApi {
    @GET("./reverse?limit=5")
    suspend fun getLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Response<List<CityLocalityResponse>>

    @GET("./direct?limit=5")
    suspend fun searchLocation(
        @Query("q") q: String,
    ): Response<List<CityLocalityResponse>>
}