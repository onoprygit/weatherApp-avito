package com.onopry.data.model.locationModels

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationResponse(
    val country: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val state: String
)