package com.onopry.data.model

import com.onopry.domain.model.forecast.Locality
import com.onopry.domain.model.forecast.LocalitySearch
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class SearchResponse(
    val cities: List<CityLocalityResponse>
)

@JsonClass(generateAdapter = true)
data class CityLocalityResponse(
    val country: String,
    val lat: Float,
    val lon: Float,
    val name: String,
)

fun CityLocalityResponse.toDomainModel() = Locality(
    country = country,
    lat = lat,
    lon = lon,
    name = name,
)

fun CityLocalityResponse.toDomainSearchModel() = LocalitySearch(
    id = UUID.randomUUID(),
    country = country,
    lat = lat,
    lon = lon,
    name = name
)

@JsonClass(generateAdapter = true)
data class CityByIpLocalityResponse(
    @Json(name = "country_code2")
    val country: String,
    @Json(name = "latitude")
    val lat: Float,
    @Json(name = "longitude")
    val lon: Float,
    @Json(name = "city")
    val name: String,
)

fun CityByIpLocalityResponse.toDomainModel() = Locality(
    country = country,
    lat = lat,
    lon = lon,
    name = name,
)