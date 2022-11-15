package com.onopry.weatherapp_avito.di

import com.onopry.data.datasources.remote.api.ForecastApi
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

@Module
@InstallIn(SingletonComponent::class)
object ForecastNetworkModule {
    @Provides
    @Singleton
    @RetrofitQualifiers.Forecast
    fun provideForecastOkHttpClient(loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    @RetrofitQualifiers.Forecast
    fun provideForecastRetrofit(moshi: Moshi, @RetrofitQualifiers.Forecast client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/v1/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideForecastApi(@RetrofitQualifiers.Forecast retrofit: Retrofit): ForecastApi = retrofit.create(
        ForecastApi::class.java)
}