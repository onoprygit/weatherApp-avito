package com.onopry.data.model

import com.onopry.domain.model.forecast.City
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityResponse (
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String
)

fun CityResponse.toDomain(): City {
    return City(
        name = name,
        lat = lat,
        lon = lon,
        countryCode = country
    )
}