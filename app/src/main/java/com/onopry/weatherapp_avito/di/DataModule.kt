package com.onopry.weatherapp_avito.di

import com.onopry.data.datasources.remote.forecast.ForecastApiDataSource
import com.onopry.data.datasources.remote.forecast.ForecastRemoteDataSource
import com.onopry.data.datasources.remote.forecast.ForecastApi
import com.onopry.data.datasources.remote.iplocation.IpGeolocationApi
import com.onopry.data.datasources.remote.iplocation.IpLocationRemoteDataSource
import com.onopry.data.datasources.remote.iplocation.IpLocationRemoteDataSourceImpl
import com.onopry.data.datasources.remote.location.BaseLocationRemoteDataSource
import com.onopry.data.datasources.remote.location.BaseLocationRemoteDataSourceImpl
import com.onopry.data.datasources.remote.location.LocationApi
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
    fun provideForecastRemoteDataSource(
        forecastApi: ForecastApi
    ): ForecastRemoteDataSource =
        ForecastApiDataSource(forecastApi = forecastApi)

    @Singleton
    @Provides
    fun provideBaseLocationRemoteDataSource(
        baseLocationApi: LocationApi
    ): BaseLocationRemoteDataSource = BaseLocationRemoteDataSourceImpl(
        api = baseLocationApi
    )

    @Singleton
    @Provides
    fun provideIpLocationRemoteDataSource(
        locationApi: IpGeolocationApi
    ): IpLocationRemoteDataSource = IpLocationRemoteDataSourceImpl(
        api = locationApi
    )

    @Singleton
    @Provides
    fun provideForecastRepository(
        forecastRemoteDataSource: ForecastRemoteDataSource,
        baseLocationSource: BaseLocationRemoteDataSource,
        ipLocationSource: IpLocationRemoteDataSource
    ): Repository =
        ForecastRepository(
            forecastSource = forecastRemoteDataSource,
            baseLocationSource = baseLocationSource,
            ipLocationSource = ipLocationSource
        )
}