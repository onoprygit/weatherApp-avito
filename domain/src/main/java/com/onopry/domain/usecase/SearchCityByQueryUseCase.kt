package com.onopry.domain.usecase

import com.onopry.domain.repository.Repository

class SearchCityByQueryUseCase(private val repo: Repository) {
    operator fun invoke(query: String) = repo.searchCities(query)
}