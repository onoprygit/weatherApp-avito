package com.onopry.data.model

import com.onopry.domain.model.forecast.Locality
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityLocationResponse(
    val country: String,
    val lat: Float,
    val lon: Float,
    val name: String,
)

fun CityLocationResponse.toDomainModel() = Locality(
    country = country,
    lat = lat,
    lon = lon,
    name = name,
)

@JsonClass(generateAdapter = true)
data class CityByIpLocationResponse(
    @Json(name = "country_code2")
    val country: String,
    @Json(name = "latitude")
    val lat: Float,
    @Json(name = "longitude")
    val lon: Float,
    @Json(name = "city")
    val name: String,
)

fun CityByIpLocationResponse.toDomainModel() = Locality(
    country = country,
    lat = lat,
    lon = lon,
    name = name,
)