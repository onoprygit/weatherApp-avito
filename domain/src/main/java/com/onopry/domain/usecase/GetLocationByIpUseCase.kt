package com.onopry.domain.usecase

import com.onopry.domain.repository.Repository

class GetLocationByIpUseCase(private val repository: Repository) {
    operator fun invoke() = repository.getLocationByIp()
}