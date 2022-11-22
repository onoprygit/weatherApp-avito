package com.onopry.weatherapp_avito.di

import com.onopry.domain.repository.Repository
import com.onopry.domain.usecase.GetForecastUseCase
import com.onopry.domain.usecase.GetLocationByIpUseCase
import com.onopry.domain.usecase.GetLocationNameUseCase
import com.onopry.domain.usecase.SearchCityByQueryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetForecastUseCase(repository: Repository) =
        GetForecastUseCase(repository = repository)

    @Provides
    fun provideGetLocationByIpUseCase(repository: Repository) =
        GetLocationByIpUseCase(repository = repository)

    @Provides
    fun provideGetLocationNameUseCase(repository: Repository) =
        GetLocationNameUseCase(repository = repository)

    @Provides
    fun provideSearchCityByQueryUseCase(repository: Repository) =
        SearchCityByQueryUseCase(repo = repository)
}