package com.onopry.data.datasources.remote.api

import com.onopry.data.model.ForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApi {

    /**
     * Getting complex forecast and current weather state.
     * [lat] and [lon] is a String of float value
     * [startDate] and [endDate] require YYYY-MM-DD date format, example: 2022-11-14
    * */

    @GET("forecast?latitude=54.96&longitude=82.85&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,precipitation,rain,showers,snowfall,freezinglevel_height,weathercode,visibility,windspeed_10m,winddirection_10m&daily=weathercode,temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset,precipitation_sum,precipitation_hours,windspeed_10m_max&current_weather=true&windspeed_unit=ms&timezone=auto&start_date=2022-11-14&end_date=2022-11-21")
    suspend fun getComplexForecast(
        @Query("latitude") lat: String,
        @Query("longitude") lon: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Response<ForecastResponse>
}