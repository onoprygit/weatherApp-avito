package com.onopry.weatherapp_avito.di

import com.onopry.domain.repository.Repository
import com.onopry.domain.usecase.GetForecastUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun getBannersAndProductsUseCase(repository: Repository) =
        GetForecastUseCase(repository = repository)
}