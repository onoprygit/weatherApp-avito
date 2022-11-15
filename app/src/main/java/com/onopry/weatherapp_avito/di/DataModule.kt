package com.onopry.weatherapp_avito.di

import com.onopry.data.datasources.remote.ForecastApiDataSource
import com.onopry.data.datasources.remote.RemoteDataSource
import com.onopry.data.datasources.remote.api.ForecastApi
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
    fun provideRemoteDataSource(api: ForecastApi): RemoteDataSource = ForecastApiDataSource(forecastApi = api)

    @Singleton
    @Provides
    fun provideForecastRepository(
        remoteDataSource: RemoteDataSource,
    ): Repository =
        ForecastRepository(forecastDataSource = remoteDataSource)
}