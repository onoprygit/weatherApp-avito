package com.onopry.data.datasources.remote.api

import com.onopry.data.model.locationModels.LocationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {
    @GET("./reverse&limit=5")
    suspend fun getLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Response<LocationResponse>
}