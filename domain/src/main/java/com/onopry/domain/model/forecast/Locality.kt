package com.onopry.domain.model.forecast

import java.util.*

data class Locality(
    val country: String? = null,
    val lat: Float,
    val lon: Float,
    val name: String? = null
)

data class LocalitySearch(
    val id: UUID,
    val country: String,
    val lat: Float,
    val lon: Float,
    val name: String,
)