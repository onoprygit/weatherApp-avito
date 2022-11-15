package com.onopry.weatherapp_avito.di

import com.onopry.data.datasources.remote.ForecastApiDataSource
import com.onopry.data.datasources.remote.RemoteDataSource
import com.onopry.data.datasources.remote.api.ForecastApi
import com.onopry.data.datasources.remote.api.LocationApi
import com.onopry.data.repository.ForecastRepository
import com.onopry.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        forecastApi: ForecastApi,
        locationApi: LocationApi
    ): RemoteDataSource =
        ForecastApiDataSource(forecastApi = forecastApi, locationApi = locationApi)

    @Singleton
    @Provides
    fun provideForecastRepository(
        remoteDataSource: RemoteDataSource,
    ): Repository =
        ForecastRepository(forecastDataSource = remoteDataSource)
}