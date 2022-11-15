package com.onopry.weatherapp_avito.di

import com.onopry.data.datasources.remote.api.ForecastApi
import com.onopry.data.datasources.remote.api.LocationApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
import com.onopry.weatherapp_avito.di.RetrofitQualifiers.Forecast
import com.onopry.weatherapp_avito.di.RetrofitQualifiers.Location
import com.onopry.weatherapp_avito.di.RetrofitQualifiers.IpGeolocation
import com.onopry.weatherapp_avito.utils.ApiKeys
import com.onopry.weatherapp_avito.utils.addQueryParam

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

}