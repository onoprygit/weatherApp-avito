package com.onopry.data.model.ipLocationModel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IpGeolocationResponse(
    @Json(name = "country_code2")
    val countryCode: String,
    @Json(name = "latitude")
    val latitude: Float,
    @Json(name = "longitude")
    val longitude: Float,
    @Json(name = "city")
    val city: String,
)