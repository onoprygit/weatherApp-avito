package com.onopry.domain.usecase

import com.onopry.domain.repository.Repository

class GetLocationNameUseCase(private val repository: Repository) {
    operator fun invoke(lat: Float, lon: Float) =
        repository.getLocationName(lat = lat.toString(), lon = lon.toString())
}
